#include <FastLED.h>
#include <SPI.h>
#include <boards.h>
#include <RBL_nRF8001.h>
#include "Boards.h"
#define NUM_LEDS 87//sollten eig 87 sein
CRGB leds1[NUM_LEDS];
CRGB leds2[NUM_LEDS];
CRGB leds3[NUM_LEDS];
CRGB leds4[10];

int equalizer_value = 0;
const int REQN = 2;
const int RDYN = 4;
static byte buf_len = 0;


bool firststart = true;
bool fadeoutlight = false;
void setup() {
  // put your setup code here, to run once:
  
  Serial.begin(115200);


  pinMode(23, INPUT);
  digitalWrite(23, HIGH);
  pinMode(25, INPUT);
  digitalWrite(25, HIGH);
  pinMode(27, INPUT);
  digitalWrite(27, HIGH);
  pinMode(31, INPUT);
  digitalWrite(31, HIGH);
  
  pinMode(22, OUTPUT);
  pinMode(24, OUTPUT);
  pinMode(26, OUTPUT);//Rechter Bildschirm
  pinMode(28, OUTPUT);//Lautsprecher
  pinMode(30, OUTPUT);//Linker Bildschirm
  pinMode(32, OUTPUT);
  pinMode(34, OUTPUT);
  pinMode(36, OUTPUT);
  pinMode(38, OUTPUT);
  pinMode(40, OUTPUT);
  
  for(int i = 22;i<45;i+=2){
    digitalWrite(i,HIGH);
  }
      ble_set_pins(REQN, RDYN);
//  // Set your BLE Shield name here, max. length 10
//  
//  // Init. and start BLE library.
    ble_begin(); 

  FastLED.addLeds<NEOPIXEL, 48>(leds1, NUM_LEDS);
  FastLED.addLeds<NEOPIXEL, 47>(leds2, NUM_LEDS);
  FastLED.addLeds<NEOPIXEL, 49>(leds3, NUM_LEDS);
  FastLED.addLeds<NEOPIXEL, 45>(leds4, 10);

  showStrip();

  firststart = true;

}
String command;
String arg1 = "";
int mode = 22;
boolean strip1 = true;
boolean strip2 = true;
boolean strip3 = true;
boolean strip4 = true;
bool newkitmode = false;


//LED zeug

CRGBPalette16 currentPalette;
TBlendType    currentBlending;
 
 
// MODE VARIABLES -- Change these numbers to create new interesting modes
 
int BRIGHTNESS = 255;    //0-255.  Lower number saves battery life, higher number is screamingly bright
int SATURATION = 255;    //0 is white (no color) and 255 is fully saturated with color
int HUE = 0;             //0-255, around the color wheel
int STEPS = 4;           //Wider or narrower bands of color
int SPEEDO = 10;         //The speed of the animation
 
// SIN WAVE SETUP
 
#define qsubd(x, b)  ((x>b)?wavebright:0)                   // Digital unsigned subtraction macro. if result <0, then => 0. Otherwise, take on fixed value.
#define qsuba(x, b)  ((x>b)?x-b:0)                          // Analog Unsigned subtraction macro. if result <0, then => 0
 
 
// SINWAVE VARIABLES -- change these numbers to modify Sinwave mode
uint8_t wavebright = 255;                                     // You can change the brightness of the waves/bars rolling across the screen.
uint8_t thishue = 30;                                          // You can change the starting hue value for the first wave.
uint8_t thisrot = 1;                                          // You can change how quickly the hue rotates for this wave. 0= color stays the same
uint8_t allsat = 255;                                         // I like 'em fully saturated with colour.
bool thisdir = 0;                                             // You can change direction.
int8_t thisspeed = 4;                                         // You can change the speed, and use negative values.
uint8_t allfreq = 25;                                         // You can change the frequency, thus overall width of bars.
int thisphase = 0;                                            // Phase change value gets calculated.
uint8_t thiscutoff = 192;                                     // You can change the cutoff value to display this wave. Lower value = longer wave.
int loopdelay = 4;                                           // You can change the delay. Also you can change the allspeed variable above. 
uint8_t bgclr = 0;                                            // A rotating background colour.
uint8_t bgbright = 55;                                        // Background color brightness
 
//RIPPLE VARIABLES
 
uint8_t colour;                                               // Ripple colour is randomized.
int center = 0;                                               // Center of the current ripple.
int step = -1;                                                // -1 is the initializing step.
uint8_t myfade = 255;                                         // Starting brightness.
#define maxsteps 16                                           // Case statement wouldn't allow a variable.

//bluetooth rgb
long cmd_rgb = 0x001030;

boolean switch1 = false;
boolean switch2 = false;
boolean switch3 = false;
int cmd_red, cmd_green, cmd_blue;

void loop() {
  //return;
  checkPanel();
  checkBLE();
  checkSerial();
  cmd_red = (cmd_rgb >> 16) & 0xFF;
  cmd_green = (cmd_rgb >> 8) & 0xFF;
  cmd_blue = cmd_rgb & 0xFF;
  if(fadeoutlight){
    colorWipe(0x00,0x00,0x00, 0);
    fadeoutlight = false;
  }
  else if(firststart && switch1){
      colorWipe(cmd_red,cmd_green,cmd_blue, 0);
      firststart = false;
  }
  else if(!switch1){
    setAll(0,0,0);
    showStrip();
  }
  else{
    switch(mode){
      case 0:
      RGBLoop();
      break;
      case 1:
      Strobe(cmd_red, cmd_green, cmd_blue, 10, 50, 1000);
      break;
      case 2:
      HalloweenEyes(cmd_red, cmd_green, cmd_blue, 1,4, true, 10, 80, 3000);
      break;
      case 3:
      CylonBounce(cmd_red, cmd_green, cmd_blue, 4, 10, 50);
      break;
      case 4:
      NewKITT(cmd_red, cmd_green, cmd_blue, 8, 10, 50);
      break; 
      case 5:
      Twinkle(cmd_red, cmd_green, cmd_blue, 10, 100, false);
      break;
      case 6:
      TwinkleRandom(20, 100, false);
      break;
      case 7:
      Sparkle(random(255), random(255), random(255), 0);
      break;
      case 8:
      SnowSparkle(0x10, 0x10, 0x10, 20, random(100,1000));
      break;
      case 9:
      RunningLights(0xff,0xff,0x00, 50);
      break;
      case 10:
      colorWipe(cmd_red, cmd_green, cmd_blue, 10);
      colorWipe(0x00,0x00,0x00, 10);
      break;
      case 11:
      rainbowCycle(20);
      break;
      case 12:
      theaterChase(0xff,0,0,50);
      break;
      case 13:
      theaterChaseRainbow(50);
      break;
      case 14:
        Fire(55,120,15);
        break;
      case 15:
      BouncingBalls(0xff,0,0, 3);
      break;
    case 16:
      BRIGHTNESS = 255; Rainbow();
      break;
    case 17:
      sinwave_1();
      break;
    case 18:
      ripple();
      break;
    case 19:
      BRIGHTNESS=30; Solid();
      break;
    case 20://bluetooth all
      setAll(cmd_red,cmd_green,cmd_blue);
      break;
  	case 21://equalizer mode
  	SetMid(equalizer_value);
  	break;
    default:
      setAll(cmd_red, cmd_green, cmd_blue);
//      newkitmode = true;
//      int high_red = cmd_red * 1.2;
//      int high_green = cmd_green * 1.2;
//      int high_blue = cmd_blue * 1.2;
//      NewKITT(high_red, high_green, high_blue, 8, 15, 50);//red green blue size speed speed
//      newkitmode = false;
      //setPixel(88,0xFF,0x00,0x50);
      break;
    }
  }
  ble_do_events();
  buf_len = 0;
}

//Schalterbelegung:
//1:31
//2:23
//3:25
//4:kaputt
//Relais:
//leer: 22, 24
//bildschirm_links:26
//bildschrim_rechts:36
//bilderschirm_oben:30
//Lautsprecher:28
//Alexa:32
void checkPanel(){
  bool tmp_save = switch1;
  switch1 = !digitalRead(31);
  if(tmp_save == false && switch1 == true){
    firststart = true;
  }else if(tmp_save == true && switch1 == false){
    fadeoutlight = true;
  }
  switch2 = !digitalRead(23);
  switch3 = !digitalRead(25);
  //digitalWrite(22,switch3 ? HIGH : LOW);
  //digitalWrite(24,switch3 ? HIGH : LOW);
  digitalWrite(26,switch3 ? HIGH : LOW);
  //digitalWrite(28,switch3 ? HIGH : LOW);
  digitalWrite(30,switch3 ? HIGH : LOW);
  //digitalWrite(32,switch3 ? HIGH : LOW);
  //digitalWrite(34,switch3 ? HIGH : LOW);
  digitalWrite(36,switch3 ? HIGH : LOW);
}

void checkBLE()
{
  command="";
  arg1="";
  int pos_str = 0;
  if(ble_available())
  {
  char inChar = char(ble_read());
    while(inChar != '\n')  //every command has to end on '\n'
    {
      if(inChar == ','){
         pos_str++;
      }else{
        if(pos_str==0){
          command+=inChar;
        }
        if(pos_str==1){
          //int num = inChar-48;
          //arg1*=10;
          arg1+=inChar;
        }
      }
      delay(2); //This delay is important to avoid reading right into the giant nothingness which lies after the last character
      inChar = char(ble_read());
    }
  }
  if(command != "" || arg1 != ""){
    if(command == "checkBLE"){
      ble_print("check");
    }
    if(command == "mode"){
      mode = arg1.toInt();
    }else if(command == "rgb"){
      for(int i = 0; i < NUM_LEDS;i++){
		cmd_rgb = arg1.toInt();
		mode = 20;
      }
    }
    command = "";
    arg1="";
  }
}

void ble_print(String output)
{//Prepares a string to be sent via BLE and then hands it to ble_write_string()
  byte buffer[output.length()+1];
  output.getBytes(buffer,output.length()+1);
  ble_write_string(buffer,output.length());
}

void ble_write_string(byte *bytes, uint8_t len)
{//sends a "string" via BLE
  if (buf_len + len > 20)
  {
    for (int j = 0; j < 15000; j++)
      ble_do_events();

    buf_len = 0;
  }

  for (int j = 0; j < len; j++)
  {
    ble_write(bytes[j]);
    buf_len++;
  }

  if (buf_len == 20)
  {
    for (int j = 0; j < 15000; j++)
      ble_do_events();

    buf_len = 0;
  }  
}

void checkSerial()
{//Checks for input from the serial connection
  //command="";
  // if(Serial.available() > 0)
  // {
    // char inChar = Serial.read();

    // while(inChar != '\n')  //every command has to end on '\n'
    // {
      // if(inChar !='\r')
      // {
        // command += inChar;
      // }
      // //while(!Serial.available()) { 
      // //}  //This is important to avoid reading right into the giant nothingness which lies after the last character
      // inChar = Serial.read();
    // }
    // mode = command.toInt();
  // }
  if(Serial.available() >= 1 && switch1){
    equalizer_value = Serial.read();
    if(mode != 21){
      setAll(0,0,0);
      mode = 21;
    }   
  }
}



//default functions
void setPixel(int Pixel, byte red, byte green, byte blue) {
  if(Pixel > NUM_LEDS||Pixel <0){
    return;
  }
   // FastLED
   if(strip1){//da is was kaputt..
     leds1[Pixel].r = red;
     leds1[Pixel].g = green;
     leds1[Pixel].b = blue;
   }
//   }else 
//  if(strip1){
//    if(red<0xA8){
//      red*=1.33;
//    }
//    if(green<0xA8){
//      green*=1.33;
//    }
//    if(blue<0xA8){
//      blue*=1.33;
//    }
//   }

   if(strip2){
     leds2[Pixel].r = red;
     leds2[Pixel].g = green;
     leds2[Pixel].b = blue;
   }

   if(strip3){
     leds3[Pixel].r = red;
     leds3[Pixel].g = green;
     leds3[Pixel].b = blue;
   }

   if(strip4){
      if(Pixel > 10){
        return;
      }
     leds4[Pixel].r = red;
     leds4[Pixel].g = green;
     leds4[Pixel].b = blue;
   }
}
void setPixelHSV(int Pixel, int Hue, int Sat, int Val){
   // FastLED
   if(strip1){
     leds1[Pixel].setHSV(Hue, Sat, Val);
   }

   if(strip2){
     leds2[Pixel].setHSV(Hue, Sat, Val);
   }

   if(strip3){
    leds3[Pixel].setHSV(Hue, Sat, Val);
   }

   if(strip4){
      if(Pixel > 10){
        return;
      }
    leds4[Pixel].setHSV(Hue, Sat, Val);
   }
}

void setAll(byte red, byte green, byte blue) {
  for(int i = 0; i < NUM_LEDS; i++ ) {
    setPixel(i, red, green, blue); 
  }
  showStrip();
}

void showStrip() {
   FastLED.show();
}
int equalizer_highest = 0;
int equalizer_highest_counter = 3;
int equalizer_highest_counter_first = 0;
void SetMid(int value){
  int num = map(value, 0, 255, 0, NUM_LEDS/2); //map value to number of leds.

  for(int i = 0; i < NUM_LEDS;i++){
    if(i >= NUM_LEDS/2-num && i <=NUM_LEDS/2+1+num && num > 0){
     
      if(NUM_LEDS/2-equalizer_highest==i || NUM_LEDS/2+1+equalizer_highest==i){
//        long highest_color = 0x0000FF;
//        leds1[NUM_LEDS/2-equalizer_highest] = highest_color;
//        leds2[NUM_LEDS/2-equalizer_highest] = highest_color;
//        leds3[NUM_LEDS/2-equalizer_highest] = highest_color;
//        leds1[NUM_LEDS/2+1+equalizer_highest] = highest_color;
//        leds2[NUM_LEDS/2+1+equalizer_highest] = highest_color;
//        leds3[NUM_LEDS/2+1+equalizer_highest] = highest_color;
          BRIGHTNESS=10;
          //setLed_LightMode(i,value);
      }else{
        BRIGHTNESS=10;
        setPixel(i,0x10,0x10,0x10);
         //setLed_LightMode(i,value);
      }
    }else{
      if(NUM_LEDS/2-equalizer_highest==i || NUM_LEDS/2+1+equalizer_highest==i){

      }else{
		    setPixel(i,0,0,0);
      }
    }
     
  }
   if(num < equalizer_highest){
      if(equalizer_highest_counter > 2){
        if(equalizer_highest >1){
          setPixel(NUM_LEDS/2-equalizer_highest,0,0,0);
          setPixel(NUM_LEDS/2+1+equalizer_highest,0,0,0);
          equalizer_highest--;
          BRIGHTNESS=255;
          //setPixel(NUM_LEDS/2-equalizer_highest,0x00,0xFF,0xFF);
          //setPixel(NUM_LEDS/2+1+equalizer_highest,0x00,0xFF,0xFF);
          setLed_LightMode(NUM_LEDS/2-equalizer_highest,value);
          setLed_LightMode(NUM_LEDS/2+1+equalizer_highest,value);
        }else{
          if(leds1[NUM_LEDS/2-1]){
            leds1[NUM_LEDS/2-1].fadeToBlackBy(50);
            leds2[NUM_LEDS/2-1].fadeToBlackBy(50);
            leds3[NUM_LEDS/2-1].fadeToBlackBy(50);
            leds1[NUM_LEDS/2+1+1].fadeToBlackBy(50);
            leds2[NUM_LEDS/2+1+1].fadeToBlackBy(50);
            leds3[NUM_LEDS/2+1+1].fadeToBlackBy(50);
          }else{
            leds1[NUM_LEDS/2].fadeToBlackBy(50);
            leds2[NUM_LEDS/2].fadeToBlackBy(50);
            leds3[NUM_LEDS/2].fadeToBlackBy(50);
            leds1[NUM_LEDS/2+1].fadeToBlackBy(50);
            leds2[NUM_LEDS/2+1].fadeToBlackBy(50);
            leds3[NUM_LEDS/2+1].fadeToBlackBy(50);
          }
        }
        equalizer_highest_counter = 0;
      }
      else{
        equalizer_highest_counter_first > 20 ? equalizer_highest_counter++ : equalizer_highest_counter_first++;
      }
    }
    else{
      setPixel(NUM_LEDS/2-equalizer_highest,0,0,0);
      setPixel(NUM_LEDS/2+1+equalizer_highest,0,0,0);
      equalizer_highest = num;
      
       BRIGHTNESS=255;//set new highest to max
      //setPixel(NUM_LEDS/2-equalizer_highest,0x00,0xFF,0xFF);
      //setPixel(NUM_LEDS/2+1+equalizer_highest,0x00,0xFF,0xFF);
      setLed_LightMode(NUM_LEDS/2-equalizer_highest,value);
      setLed_LightMode(NUM_LEDS/2+1+equalizer_highest,value);
      
      equalizer_highest_counter_first = 0;
      equalizer_highest_counter = 3;
    } 
  FastLED.show();
}
int lightmode =4;
void setLed_LightMode(int led_num, int value){
  int color;
  switch(lightmode){
    case 0://blue to red
      setPixel(led_num,value,255-value,255-value);
      break;
    case 1://brightness
      setPixel(led_num,value,value,value);
      break;
    case 2://rainbow_value
      setPixelHSV(led_num,value,0xFF,0xFF);
      break;
    case 3://rainbow mid
      if(led_num>NUM_LEDS/2){
        color = map(led_num,0,255,255,0);
      }else{
        color = map(led_num,0,255,0,255);
      }
      setPixelHSV(led_num,color,0xFF,0xFF);
      //leds[led_num].setHue(color);todo
      break;
   case 4://blue to red
      if(led_num>NUM_LEDS/2){
        color = map(led_num,NUM_LEDS/2,NUM_LEDS,BRIGHTNESS,0);
      }else{
        color = map(led_num,0,NUM_LEDS/2,0,BRIGHTNESS);
      }
      setPixel(led_num,BRIGHTNESS-color,color,0);
      break;
  }
}

boolean delay_read(int time_ms){
  long cur_time = millis();
  while(millis() - cur_time < time_ms){
    int savemode = mode;
    checkSerial();
    checkBLE();
    ble_do_events();
    buf_len = 0;
    showStrip();
    if(savemode != mode)
      return true;
  }
  return false;
//int savemode = mode;
//      checkSerial();
//    checkBLE();
//    ble_do_events();
//    buf_len = 0;
//    if(savemode != mode)
//      return true;
//     else{
//      delay(time_ms);
//      return false;
//     }
// return false; 
}


void RGBLoop(){
  for(int j = 0; j < 3; j++ ) { 
    // Fade IN
    for(int k = 0; k < 256; k++) { 
      switch(j) { 
        case 0: setAll(k,0,0); break;
        case 1: setAll(0,k,0); break;
        case 2: setAll(0,0,k); break;
      }
      showStrip();
      if(delay_read(3))
        return;
    }
    // Fade OUT
    for(int k = 255; k >= 0; k--) { 
      switch(j) { 
        case 0: setAll(k,0,0); break;
        case 1: setAll(0,k,0); break;
        case 2: setAll(0,0,k); break;
      }
      showStrip();
      if(delay_read(3))
        return;
    }
  }
}

//Fade Effect - not implemented in loop!
void FadeInOut(byte red, byte green, byte blue){
  float r, g, b;
      
  for(int k = 0; k < 256; k=k+1) { 
    r = (k/256.0)*red;
    g = (k/256.0)*green;
    b = (k/256.0)*blue;
    setAll(r,g,b);
    showStrip();
  }
     
  for(int k = 255; k >= 0; k=k-2) {
    r = (k/256.0)*red;
    g = (k/256.0)*green;
    b = (k/256.0)*blue;
    setAll(r,g,b);
    showStrip();
  }
}

void Strobe(byte red, byte green, byte blue, int StrobeCount, int FlashDelay, int EndPause){
  for(int j = 0; j < StrobeCount; j++) {
    setAll(red,green,blue);
    showStrip();
    if(delay_read(FlashDelay))
    return;
    setAll(0,0,0);
    showStrip();
    if(delay_read(FlashDelay))
    return;
  }
 
 if(delay_read(EndPause))
  return;
}


void HalloweenEyes(byte red, byte green, byte blue, 
                   int EyeWidth, int EyeSpace, 
                   boolean Fade, int Steps, int FadeDelay,
                   int EndPause){
  randomSeed(analogRead(0));
  
  int i;
  int StartPoint  = random( 0, NUM_LEDS - (2*EyeWidth) - EyeSpace );
  int Start2ndEye = StartPoint + EyeWidth + EyeSpace;
  
  for(i = 0; i < EyeWidth; i++) {
    setPixel(StartPoint + i, red, green, blue);
    setPixel(Start2ndEye + i, red, green, blue);
  }
  
  showStrip();
  
  if(Fade==true) {
    float r, g, b;
  
    for(int j = Steps; j >= 0; j--) {
      r = j*(red/Steps);
      g = j*(green/Steps);
      b = j*(blue/Steps);
      
      for(i = 0; i < EyeWidth; i++) {
        setPixel(StartPoint + i, r, g, b);
        setPixel(Start2ndEye + i, r, g, b);
      }
      
      showStrip();
      if(delay_read(FadeDelay))
    return;
    }
  }
  
  setAll(0,0,0); // Set all black
  
  if(delay_read(EndPause))
  return;
}
void CylonBounce(byte red, byte green, byte blue, int EyeSize, int SpeedDelay, int ReturnDelay){

  for(int i = 0; i < NUM_LEDS-EyeSize-2; i++) {
    setAll(0,0,0);
    setPixel(i, red/10, green/10, blue/10);
    for(int j = 1; j <= EyeSize; j++) {
      setPixel(i+j, red, green, blue); 
    }
    setPixel(i+EyeSize+1, red/10, green/10, blue/10);
    showStrip();
    if(delay_read(SpeedDelay))
    return;
  }

  if(delay_read(ReturnDelay))
  return;

  for(int i = NUM_LEDS-EyeSize-2; i > 0; i--) {
    setAll(0,0,0);
    setPixel(i, red/10, green/10, blue/10);
    for(int j = 1; j <= EyeSize; j++) {
      setPixel(i+j, red, green, blue); 
    }
    setPixel(i+EyeSize+1, red/10, green/10, blue/10);
    showStrip();
    if(delay_read(SpeedDelay))
      return;
  }
  
  if(delay_read(ReturnDelay))
  return;
}


//KITT:
void NewKITT(byte red, byte green, byte blue, int EyeSize, int SpeedDelay, int ReturnDelay){
  int savemode = mode;
  RightToLeft(red, green, blue, EyeSize, SpeedDelay, ReturnDelay);
  if(savemode != mode)
    return;
  LeftToRight(red, green, blue, EyeSize, SpeedDelay, ReturnDelay);
  if(savemode != mode)
    return;
  OutsideToCenter(red, green, blue, EyeSize, SpeedDelay, ReturnDelay);
  if(savemode != mode)
    return;
  CenterToOutside(red, green, blue, EyeSize, SpeedDelay, ReturnDelay);
  if(savemode != mode)
    return;
  LeftToRight(red, green, blue, EyeSize, SpeedDelay, ReturnDelay);
  if(savemode != mode)
    return;
  RightToLeft(red, green, blue, EyeSize, SpeedDelay, ReturnDelay);
  if(savemode != mode)
    return;
  OutsideToCenter(red, green, blue, EyeSize, SpeedDelay, ReturnDelay);
  if(savemode != mode)
    return;
  CenterToOutside(red, green, blue, EyeSize, SpeedDelay, ReturnDelay);
}

void CenterToOutside(byte red, byte green, byte blue, int EyeSize, int SpeedDelay, int ReturnDelay) {
  if(newkitmode){
      setAll(cmd_red,cmd_green,cmd_blue);
    }else{
      setAll(0,0,0);
    }
  for(int i =((NUM_LEDS-EyeSize)/2); i>=0; i--) {
    if(newkitmode){
      setPixel(i, red/10+cmd_red, green/10+cmd_green, blue/10+cmd_blue);
    }else{
      setPixel(i, red/10, green/10, blue/10);
    }
    for(int j = 1; j <= EyeSize; j++) {
      setPixel(i+j, red, green, blue); 
    }
    if(newkitmode){
      setPixel(i+EyeSize+1, red/10+cmd_red, green/10+cmd_green, blue/10+cmd_blue);
    }else{
      setPixel(i+EyeSize+1, red/10, green/10, blue/10);
    }
    setPixel(i+EyeSize+2, cmd_red, cmd_green, cmd_blue);

    if(newkitmode){
      setPixel(NUM_LEDS-i, red/10+cmd_red, green/10+cmd_green, blue/10+cmd_blue);
    }else{
      setPixel(NUM_LEDS-i, red/10, green/10, blue/10);
    }

    for(int j = 1; j <= EyeSize; j++) {
      setPixel(NUM_LEDS-i-j, red, green, blue); 
    }
    if(newkitmode){
      setPixel(NUM_LEDS-i-EyeSize-1, red/10+cmd_red, green/10+cmd_green, blue/10+cmd_blue);
    }else{
      setPixel(NUM_LEDS-i-EyeSize-1, red/10, green/10, blue/10);
    }
    setPixel(NUM_LEDS-i-EyeSize-2, cmd_red, cmd_green, cmd_blue);
    
    showStrip();
    if(delay_read(SpeedDelay))
    return;
  }
  if(delay_read(ReturnDelay))
  return;
}

void OutsideToCenter(byte red, byte green, byte blue, int EyeSize, int SpeedDelay, int ReturnDelay) {
    if(newkitmode){
      setAll(cmd_red,cmd_green,cmd_blue);
    }else{
      setAll(0,0,0);
    }
    
  for(int i = 0; i<=((NUM_LEDS-EyeSize)/2); i++) {

    setPixel(i-1, cmd_red, cmd_green, cmd_blue);
    if(newkitmode){
      setPixel(i, red/10+cmd_red, green/10+cmd_green, blue/10+cmd_blue);
    }else{
      setPixel(i, red/10, green/10, blue/10);
    }
    for(int j = 1; j <= EyeSize; j++) {
      setPixel(i+j, red, green, blue); 
    }

    if(newkitmode){
      setPixel(i+EyeSize+1, red/10+cmd_red, green/10+cmd_green, blue/10+cmd_blue);
    }else{
      setPixel(i+EyeSize+1, red/10, green/10, blue/10);
    }
    

    setPixel(NUM_LEDS-i+1, cmd_red, cmd_green, cmd_blue);
    if(newkitmode){
      setPixel(NUM_LEDS-i, red/10+cmd_red, green/10+cmd_green, blue/10+cmd_blue);
    }else{
      setPixel(NUM_LEDS-i, red/10, green/10, blue/10);
    }
    for(int j = 1; j <= EyeSize; j++) {
      setPixel(NUM_LEDS-i-j, red, green, blue); 
    }
    setPixel(NUM_LEDS-i-EyeSize-1, red/10, green/10, blue/10);
    
    
    showStrip();
    if(delay_read(SpeedDelay))
    return;
  }
  if(delay_read(ReturnDelay))
  return;
}

void LeftToRight(byte red, byte green, byte blue, int EyeSize, int SpeedDelay, int ReturnDelay) {
     if(newkitmode){
      setAll(cmd_red,cmd_green,cmd_blue);
    }else{
      setAll(0,0,0);
    }
  for(int i = 0; i < NUM_LEDS-EyeSize-2; i++) {
    setPixel(i-1, cmd_red, cmd_green, cmd_blue);
    if(newkitmode){
      setPixel(i, red/10+cmd_red, green/10+cmd_green, blue/10+cmd_blue);
    }else{
      setPixel(i, red/10, green/10, blue/10);
    }
    for(int j = 1; j <= EyeSize; j++) {
      setPixel(i+j, red, green, blue); 
    }
    if(newkitmode){
      setPixel(i+EyeSize+1, red/10+cmd_red, green/10+cmd_green, blue/10+cmd_blue);
    }else{
      setPixel(i+EyeSize+1, red/10, green/10, blue/10);
    }
    showStrip();
    if(delay_read(SpeedDelay))
    return;
  }
  if(delay_read(ReturnDelay))
  return;
}

void RightToLeft(byte red, byte green, byte blue, int EyeSize, int SpeedDelay, int ReturnDelay) {
   if(newkitmode){
      setAll(cmd_red,cmd_green,cmd_blue);
    }else{
      setAll(0,0,0);
    }
  for(int i = NUM_LEDS-EyeSize-2; i > 0; i--) {
    if(newkitmode){
      setPixel(i, red/10+cmd_red, green/10+cmd_green, blue/10+cmd_blue);
    }else{
      setPixel(i, red/10, green/10, blue/10);
    }
    for(int j = 1; j <= EyeSize; j++) {
      setPixel(i+j, red, green, blue); 
    }
    if(newkitmode){
      setPixel(i+EyeSize+1, red/10+cmd_red, green/10+cmd_green, blue/10+cmd_blue);
    }else{
      setPixel(i+EyeSize+1, red/10, green/10, blue/10);
    }
    setPixel(i+EyeSize+2, cmd_red, cmd_green, cmd_blue);
    showStrip();
    if(delay_read(SpeedDelay))
    return;
  }
  if(delay_read(ReturnDelay))
  return;
}

void Twinkle(byte red, byte green, byte blue, int Count, int SpeedDelay, boolean OnlyOne) {
  setAll(0,0,0);
  
  for (int i=0; i<Count; i++) {
     setPixel(random(NUM_LEDS),red,green,blue);
     showStrip();
     if(delay_read(SpeedDelay))
    return;
     if(OnlyOne) { 
       setAll(0,0,0); 
     }
   }
  
  if(delay_read(SpeedDelay))
  return;
}

void TwinkleRandom(int Count, int SpeedDelay, boolean OnlyOne) {
  setAll(0,0,0);
  
  for (int i=0; i<Count; i++) {
     setPixel(random(NUM_LEDS),random(0,255),random(0,255),random(0,255));
     showStrip();
     if(delay_read(SpeedDelay))
    return;
     if(OnlyOne) { 
       setAll(0,0,0); 
     }
   }
  
  if(delay_read(SpeedDelay))
  return;
}

void Sparkle(byte red, byte green, byte blue, int SpeedDelay) {
  int Pixel = random(NUM_LEDS);
  setPixel(Pixel,red,green,blue);
  showStrip();
  if(delay_read(SpeedDelay))
  return;
  setPixel(Pixel,0,0,0);
}

void SnowSparkle(byte red, byte green, byte blue, int SparkleDelay, int SpeedDelay) {
  setAll(red,green,blue);
  
  int Pixel = random(NUM_LEDS);
  setPixel(Pixel,0xff,0xff,0xff);
  showStrip();
  if(delay_read(SparkleDelay))
  return;
  setPixel(Pixel,red,green,blue);
  showStrip();
  if(delay_read(SpeedDelay))
  return;
}

void RunningLights(byte red, byte green, byte blue, int WaveDelay) {
  int Position=0;
  
  for(int i=0; i<NUM_LEDS*2; i++)
  {
      Position++; // = 0; //Position + Rate;
      for(int i=0; i<NUM_LEDS; i++) {
        // sine wave, 3 offset waves make a rainbow!
        //float level = sin(i+Position) * 127 + 128;
        //setPixel(i,level,0,0);
        //float level = sin(i+Position) * 127 + 128;
        setPixel(i,((sin(i+Position) * 127 + 128)/255)*red,
                   ((sin(i+Position) * 127 + 128)/255)*green,
                   ((sin(i+Position) * 127 + 128)/255)*blue);
      }
      
      showStrip();
      if(delay_read(WaveDelay))
    return;
  }
}

void colorWipe(byte red, byte green, byte blue, int SpeedDelay) {
  for(uint16_t i=0; i<NUM_LEDS; i++) {
      setPixel(i, red, green, blue);
      showStrip();
      if(delay_read(SpeedDelay))
    return;
  }
}

void rainbowCycle(int SpeedDelay) {
  byte *c;
  uint16_t i, j;

  for(j=0; j<256*5; j++) { // 5 cycles of all colors on wheel
    for(i=0; i< NUM_LEDS; i++) {
      c=Wheel(((i * 256 / NUM_LEDS) + j) & 255);
      setPixel(i, *c, *(c+1), *(c+2));
    }
    showStrip();
    if(delay_read(SpeedDelay))
      return;
  }
}

byte * Wheel(byte WheelPos) {
  static byte c[3];
  
  if(WheelPos < 85) {
   c[0]=WheelPos * 3;
   c[1]=255 - WheelPos * 3;
   c[2]=0;
  } else if(WheelPos < 170) {
   WheelPos -= 85;
   c[0]=255 - WheelPos * 3;
   c[1]=0;
   c[2]=WheelPos * 3;
  } else {
   WheelPos -= 170;
   c[0]=0;
   c[1]=WheelPos * 3;
   c[2]=255 - WheelPos * 3;
  }

  return c;
}

void theaterChase(byte red, byte green, byte blue, int SpeedDelay) {
  for (int j=0; j<10; j++) {  //do 10 cycles of chasing
    for (int q=0; q < 3; q++) {
      for (int i=0; i < NUM_LEDS; i=i+3) {
        setPixel(i+q, red, green, blue);    //turn every third pixel on
      }
      showStrip();
     
      if(delay_read(SpeedDelay))
    return;
     
      for (int i=0; i < NUM_LEDS; i=i+3) {
        setPixel(i+q, 0,0,0);        //turn every third pixel off
      }
    }
  }
}

void BouncingBalls(byte red, byte green, byte blue, int BallCount) {
  float Gravity = -9.81;
  int StartHeight = 1;
  
  float Height[BallCount];
  float ImpactVelocityStart = sqrt( -2 * Gravity * StartHeight );
  float ImpactVelocity[BallCount];
  float TimeSinceLastBounce[BallCount];
  int   Position[BallCount];
  long  ClockTimeSinceLastBounce[BallCount];
  float Dampening[BallCount];
  
  for (int i = 0 ; i < BallCount ; i++) {   
    ClockTimeSinceLastBounce[i] = millis();
    Height[i] = StartHeight;
    Position[i] = 0; 
    ImpactVelocity[i] = ImpactVelocityStart;
    TimeSinceLastBounce[i] = 0;
    Dampening[i] = 0.90 - float(i)/pow(BallCount,2); 
  }

  while (true) {
    for (int i = 0 ; i < BallCount ; i++) {
      TimeSinceLastBounce[i] =  millis() - ClockTimeSinceLastBounce[i];
      Height[i] = 0.5 * Gravity * pow( TimeSinceLastBounce[i]/1000 , 2.0 ) + ImpactVelocity[i] * TimeSinceLastBounce[i]/1000;
  
      if ( Height[i] < 0 ) {                      
        Height[i] = 0;
        ImpactVelocity[i] = Dampening[i] * ImpactVelocity[i];
        ClockTimeSinceLastBounce[i] = millis();
  
        if ( ImpactVelocity[i] < 0.01 ) {
          ImpactVelocity[i] = ImpactVelocityStart;
        }
      }
      Position[i] = round( Height[i] * (NUM_LEDS - 1) / StartHeight);
    }
  
    for (int i = 0 ; i < BallCount ; i++) {
      setPixel(Position[i],red,green,blue);
    }
  if(delay_read(1))
    return;
    
    showStrip();
    setAll(0,0,0);
  }
}
void theaterChaseRainbow(int SpeedDelay) {
  byte *c;
  
  for (int j=0; j < 256; j++) {     // cycle all 256 colors in the wheel
    for (int q=0; q < 3; q++) {
        for (int i=0; i < NUM_LEDS; i=i+3) {
          c = Wheel( (i+j) % 255);
          setPixel(i+q, *c, *(c+1), *(c+2));    //turn every third pixel on
        }
        showStrip();
       
        if(delay_read(SpeedDelay))
      return;
       
        for (int i=0; i < NUM_LEDS; i=i+3) {
          setPixel(i+q, 0,0,0);        //turn every third pixel off
        }
    }
  }
}

void Fire(int Cooling, int Sparking, int SpeedDelay) {
  static byte heat[NUM_LEDS];
  int cooldown;
  
  // Step 1.  Cool down every cell a little
  for( int i = 0; i < NUM_LEDS; i++) {
    cooldown = random(0, ((Cooling * 10) / NUM_LEDS) + 2);
    
    if(cooldown>heat[i]) {
      heat[i]=0;
    } else {
      heat[i]=heat[i]-cooldown;
    }
  }
  
  // Step 2.  Heat from each cell drifts 'up' and diffuses a little
  for( int k= NUM_LEDS - 1; k >= 2; k--) {
    heat[k] = (heat[k - 1] + heat[k - 2] + heat[k - 2]) / 3;
  }
    
  // Step 3.  Randomly ignite new 'sparks' near the bottom
  if( random(255) < Sparking ) {
    int y = random(7);
    heat[y] = heat[y] + random(160,255);
    //heat[y] = random(160,255);
  }

  // Step 4.  Convert heat to LED colors
  for( int j = 0; j < NUM_LEDS; j++) {
    setPixelHeatColor(j, heat[j] );
  }

  showStrip();
  if(delay_read(SpeedDelay))
  return;
}

void setPixelHeatColor (int Pixel, byte temperature) {
  // Scale 'heat' down from 0-255 to 0-191
  byte t192 = round((temperature/255.0)*191);
 
  // calculate ramp up from
  byte heatramp = t192 & 0x3F; // 0..63
  heatramp <<= 2; // scale up to 0..252
 
  // figure out which third of the spectrum we're in:
  if( t192 > 0x80) {                     // hottest
    setPixel(Pixel, 255, 255, heatramp);
  } else if( t192 > 0x40 ) {             // middle
    setPixel(Pixel, 255, heatramp, 0);
  } else {                               // coolest
    setPixel(Pixel, heatramp, 0, 0);
  }
}


// SOLID COLOR -------------------------------------
void Solid()
{
   fill_solid(leds1, NUM_LEDS, CHSV(HUE, SATURATION, BRIGHTNESS));  
   fill_solid(leds2, NUM_LEDS, CHSV(HUE, SATURATION, BRIGHTNESS));  
   fill_solid(leds3, NUM_LEDS, CHSV(HUE, SATURATION, BRIGHTNESS));  
   FastLED.show(); 
}
 
 
//SIN WAVE
void sinwave_1() {
   one_sin();  
   show_at_max_brightness_for_power();
    delay_at_max_brightness_for_power(loopdelay*2.5);
    Serial.println(LEDS.getFPS());   
}
 
void one_sin() {                                                                // This is the heart of this program. Sure is short.
  if (thisdir == 0) thisphase+=thisspeed; else thisphase-=thisspeed;            // You can change direction and speed individually.
    thishue = thishue + thisrot;                                                // Hue rotation is fun for thiswave.
  for (int k=0; k<NUM_LEDS; k++) {
    int thisbright = qsubd(cubicwave8((k*allfreq)+thisphase), thiscutoff);      // qsub sets a minimum value called thiscutoff. If < thiscutoff, then bright = 0. Otherwise, bright = 128 (as defined in qsub)..
    leds1[k] = CHSV(bgclr, 255, bgbright);
    leds1[k] += CHSV(thishue, allsat, thisbright);                               // Assigning hues and brightness to the led array.
  leds2[k] = CHSV(bgclr, 255, bgbright);
    leds2[k] += CHSV(thishue, allsat, thisbright);                               // Assigning hues and brightness to the led array.
  leds3[k] = CHSV(bgclr, 255, bgbright);
    leds3[k] += CHSV(thishue, allsat, thisbright);                               // Assigning hues and brightness to the led array.
  }
  bgclr++;
} // one_sin()
 
 
 
// RAINBOW --------------------------------------------------
void Rainbow()
{ 
  FastLED.setBrightness(  BRIGHTNESS );
  currentPalette = RainbowColors_p;
  
  static uint8_t startIndex = 0;
  startIndex = startIndex + 1; 
 
  FillLEDsFromPaletteColors( startIndex);
    
  FastLED.show();
  FastLED.delay(SPEEDO);  
}
 
void FillLEDsFromPaletteColors( uint8_t colorIndex)
{
  
  for( int i = 0; i < NUM_LEDS; i++) {
    leds1[i] = ColorFromPalette( currentPalette, colorIndex, BRIGHTNESS, currentBlending);
  leds2[i] = ColorFromPalette( currentPalette, colorIndex, BRIGHTNESS, currentBlending);
  leds3[i] = ColorFromPalette( currentPalette, colorIndex, BRIGHTNESS, currentBlending);
    colorIndex += STEPS;
  }
}
 
//RIPPLE --------------------------------------------------------------------------------
void ripple() {
  HUE = 140;HUE++; 
    if (HUE > 220) {HUE = 140;}   // constrain BG hue to blues and purples
  for (int i = 0; i < NUM_LEDS; i++) {
    leds1[i] = CHSV(HUE++, 255, 50);  // Rotate background colour.
    leds2[i] = CHSV(HUE++, 255, 50);  // Rotate background colour.
    leds3[i] = CHSV(HUE++, 255, 50);  // Rotate background colour.
  }
 
  switch (step) {
 
    case -1:                                                          // Initialize ripple variables.
      center = random(NUM_LEDS);
      colour = random16(0,256);
      step = 0;
      break;
 
    case 0:
      leds1[center] = CHSV(colour, 255, 255);                          // Display the first pixel of the ripple.
      leds2[center] = CHSV(colour, 255, 255);                          // Display the first pixel of the ripple.
      leds3[center] = CHSV(colour, 255, 255);                          // Display the first pixel of the ripple.
      step ++;
      break;
 
    case maxsteps:                                                    // At the end of the ripples.
      step = -1;
      break;
 
    default:                                                          // Middle of the ripples.
        leds1[wrap(center + step)] += CHSV(colour, 255, myfade/step*2);   // Display the next pixels in the range for one side.
        leds2[wrap(center + step)] += CHSV(colour, 255, myfade/step*2);   // Display the next pixels in the range for one side.
        leds3[wrap(center + step)] += CHSV(colour, 255, myfade/step*2);   // Display the next pixels in the range for one side.
        leds1[wrap(center - step)] += CHSV(colour, 255, myfade/step*2);   // Display the next pixels in the range for the other side.
        leds2[wrap(center - step)] += CHSV(colour, 255, myfade/step*2);   // Display the next pixels in the range for the other side.
        leds3[wrap(center - step)] += CHSV(colour, 255, myfade/step*2);   // Display the next pixels in the range for the other side.
        step ++;                                                      // Next step.
        break;  
  } // switch step
  
 show_at_max_brightness_for_power();
  delay_at_max_brightness_for_power(SPEEDO*2.5);  
} // ripple()
 
 
 
int wrap(int step) {
  if(step < 0) return NUM_LEDS + step;
  if(step > NUM_LEDS - 1) return step - NUM_LEDS;
  return step;
} // wrap()
 
