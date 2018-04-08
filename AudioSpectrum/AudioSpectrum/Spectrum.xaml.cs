using System.Collections.Generic;
using System.Windows.Controls;

namespace AudioSpectrum
{
    /// <summary>
    /// Interaction logic for Spectrum.xaml
    /// </summary>
    public partial class Spectrum : UserControl
    {
        public Spectrum()
        {
            InitializeComponent();
        }

        public void Set(List<byte> data)
        {
            if (data.Count < 1) return;
            Bar01.Value = data[0];
            Bar02.Value = (data.Count < 2) ? 0 : data[1];
            Bar03.Value = (data.Count < 3) ? 0 : data[2];
            Bar04.Value = (data.Count < 4) ? 0 : data[3];
            Bar05.Value = (data.Count < 5) ? 0 : data[4];
            Bar06.Value = (data.Count < 6) ? 0 : data[5];
            Bar07.Value = (data.Count < 7) ? 0 : data[6];
            Bar08.Value = (data.Count < 8) ? 0 : data[7];
            Bar09.Value = (data.Count < 9) ? 0 : data[8];
            Bar10.Value = (data.Count < 10) ? 0 : data[9];
            Bar11.Value = (data.Count < 11) ? 0 : data[10];
            Bar12.Value = (data.Count < 12) ? 0 : data[11];
            Bar13.Value = (data.Count < 13) ? 0 : data[12];
            Bar14.Value = (data.Count < 14) ? 0 : data[13];
            Bar15.Value = (data.Count < 15) ? 0 : data[14];
            Bar16.Value = (data.Count < 16) ? 0 : data[15];
            //Bar01.Value = data[0];
        }
    }
}
