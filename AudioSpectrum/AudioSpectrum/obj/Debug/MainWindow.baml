   M S B A M L   `   `   `  ����H  DAudioSpectrum, Version=1.0.0.0, Culture=neutral, PublicKeyToken=null-clr-namespace:AudioSpectrumAudioSpectrum  5   	       AudioSpectrum.MainWindow   5   	   \ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35D 9http://schemas.microsoft.com/winfx/2006/xaml/presentation    5   	   R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     5   	   -localclr-namespace:AudioSpectrum     5   	   -   5          �Name$
  main��5   	    � Title$ Spectrum Daemon��5   	   $	��230��6!   $	��600��6.    � WindowStyle$ 
ToolWindow=�6:    � Topmost 6S   ��  �� ElementName$ Alwaystp�� �� Path$ 	IsChecked�.��5      � #��� ��6    � ColumnDefinitions 5   
   �� 5	      $	��100 �6   �� 5
       � RowDefinitions 5   
   �� 5      $��28 �6   �� 5      $��28 �6   �� 5      $��28 �6   �� 5      �� 5      $��28 �6   .+�5   
   �� $��Device:��6   $��Center=�6#   $��Center=�6>   �� 5   
   -   5         ��Name$ 	DeviceBox��5      $��1��6&   $��Center=�63   �� 5   
   $��On/Off:��6   $��Center=�6#   $��2��6>   $��Center=�6K   d� 5   
   -   5        	 d�Name$	 	BtnEnable��5      $��Enable��6*   $��3��6;   $��10q�6H   6T      AudioSpectrum.Spectrum  5   
   -   5        
  Name$
 Spectrum��5      $��1��6+   $��3��68   $��1��6I   $��Center=�6Y   $��	5,0,167,0q�6v   �� 5   
   $��0��6    �� Orientation$ 
Horizontal=�6"   $��1��6;   .+�5      �� $��L:��6   $��Center=�6"   $
��10,0q�6=   � 5      -   5         �Name$	 PbL��5      $	��210��6'   $��15��63   �� 5      $��R:��6   $��Center=�6"   $
��10,0q�6=   � 5      -   5        $	 PbR��5      $	��210��6'   $��15��63   5      �� 5   
   $ 
Horizontal=�6   $��4��6.   $��2��6;   $��Center=�6O   .+�5      �� $��	Com Port:��6   $��Center=�6)   $	��5,0q�6D   �� 5       -   5        $ Comports��5       $	��120��6)   $��Center=�65   $	��5,0q�6P   6]   �� 5!      -   5         ��Name$ 	CkbSerial��5!      $��Enable Serial output��6*   $��Center=�6I   $	��5,0q�6d   6q   �� 5"      -	   5        $ 
CkbDisplay��5"      $��Enable software display��6+   $��Center=�6M   $	��5,0q�6h   6u   �� 5#      -
   5        $ Alwaystp��5#      $��Always on top��6)   $��Center=�6A    d� 	IsChecked$
 TrueY�6\   5$      �� 5%   
   -   5        $ comboBox_count��5%      $��1��6+   $
��Left=�6;   $��	330,7,0,0q�6V   $��1��6i   $	��Top=�6v   $	��120��6�   6�   �� 5&   
   -   5        $ comboBox_selected��5&      $��1��6.   $
��Left=�6>   $��	330,7,0,0q�6Y   $��2��6l   $	��Top=�6y   $	��120��6�   6�   5'      5(      