using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using IrpyteRunner.Properties;

namespace IrpyteRunner
{
    public partial class MainWindow : Form
    {
        public MainWindow()
        {
            this.Icon = Icon.FromHandle(Resources.icon.GetHicon());
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {
        }

        private void button2_Click(object sender, EventArgs e)
        {
        }

        private void MainWindow_Load(object sender, EventArgs e)
        {
        }
    }
}