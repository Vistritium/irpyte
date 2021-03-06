﻿using System.Windows.Forms;

namespace IrpyteRunner
{
    partial class MainWindow
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.panel1 = new System.Windows.Forms.Panel();
            this.newVersionLabel = new System.Windows.Forms.LinkLabel();
            this.progressIndicator = new System.Windows.Forms.PictureBox();
            this.searchButton = new System.Windows.Forms.Button();
            this.searchBox = new System.Windows.Forms.TextBox();
            this.findNewButton = new System.Windows.Forms.Button();
            this.pictureBox1 = new System.Windows.Forms.PictureBox();
            this.poweredByLabel = new System.Windows.Forms.LinkLabel();
            this.iconByLabel = new System.Windows.Forms.LinkLabel();
            this.panel1.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.progressIndicator)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.pictureBox1)).BeginInit();
            this.SuspendLayout();
            // 
            // panel1
            // 
            this.panel1.Controls.Add(this.newVersionLabel);
            this.panel1.Controls.Add(this.progressIndicator);
            this.panel1.Controls.Add(this.searchButton);
            this.panel1.Controls.Add(this.searchBox);
            this.panel1.Controls.Add(this.findNewButton);
            this.panel1.Location = new System.Drawing.Point(12, 12);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(760, 60);
            this.panel1.TabIndex = 0;
            // 
            // newVersionLabel
            // 
            this.newVersionLabel.AutoSize = true;
            this.newVersionLabel.Font = new System.Drawing.Font("Franklin Gothic Medium", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.newVersionLabel.Location = new System.Drawing.Point(667, 9);
            this.newVersionLabel.Name = "newVersionLabel";
            this.newVersionLabel.Size = new System.Drawing.Size(90, 42);
            this.newVersionLabel.TabIndex = 4;
            this.newVersionLabel.TabStop = true;
            this.newVersionLabel.Text = "new version\r\navailable";
            this.newVersionLabel.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            this.newVersionLabel.LinkClicked += new System.Windows.Forms.LinkLabelLinkClickedEventHandler(this.newVersionLabel_LinkClicked);
            // 
            // progressIndicator
            // 
            this.progressIndicator.Image = global::IrpyteRunner.Properties.Resources.progress_indicator;
            this.progressIndicator.Location = new System.Drawing.Point(208, 3);
            this.progressIndicator.Name = "progressIndicator";
            this.progressIndicator.Size = new System.Drawing.Size(115, 54);
            this.progressIndicator.TabIndex = 3;
            this.progressIndicator.TabStop = false;
            this.progressIndicator.Visible = false;
            // 
            // searchButton
            // 
            this.searchButton.BackgroundImage = global::IrpyteRunner.Properties.Resources.search_icon;
            this.searchButton.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
            this.searchButton.Location = new System.Drawing.Point(609, 13);
            this.searchButton.Name = "searchButton";
            this.searchButton.Size = new System.Drawing.Size(39, 37);
            this.searchButton.TabIndex = 2;
            this.searchButton.UseVisualStyleBackColor = true;
            this.searchButton.Click += new System.EventHandler(this.searchButton_Click);
            // 
            // searchBox
            // 
            this.searchBox.AutoCompleteMode = System.Windows.Forms.AutoCompleteMode.Suggest;
            this.searchBox.AutoCompleteSource = System.Windows.Forms.AutoCompleteSource.CustomSource;
            this.searchBox.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(255)))), ((int)(((byte)(253)))), ((int)(((byte)(240)))));
            this.searchBox.Font = new System.Drawing.Font("Microsoft Sans Serif", 18F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.searchBox.Location = new System.Drawing.Point(329, 14);
            this.searchBox.Name = "searchBox";
            this.searchBox.Size = new System.Drawing.Size(274, 35);
            this.searchBox.TabIndex = 1;
            this.searchBox.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.searchBox_KeyPress);
            // 
            // findNewButton
            // 
            this.findNewButton.Anchor = System.Windows.Forms.AnchorStyles.Left;
            this.findNewButton.DialogResult = System.Windows.Forms.DialogResult.Retry;
            this.findNewButton.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.findNewButton.Location = new System.Drawing.Point(16, 12);
            this.findNewButton.Name = "findNewButton";
            this.findNewButton.Size = new System.Drawing.Size(186, 37);
            this.findNewButton.TabIndex = 0;
            this.findNewButton.Text = "Find New Wallpaper";
            this.findNewButton.UseVisualStyleBackColor = true;
            this.findNewButton.Click += new System.EventHandler(this.findNewButton_Click);
            // 
            // pictureBox1
            // 
            this.pictureBox1.Location = new System.Drawing.Point(-2, 78);
            this.pictureBox1.Name = "pictureBox1";
            this.pictureBox1.Size = new System.Drawing.Size(800, 600);
            this.pictureBox1.SizeMode = System.Windows.Forms.PictureBoxSizeMode.Zoom;
            this.pictureBox1.TabIndex = 1;
            this.pictureBox1.TabStop = false;
            // 
            // poweredByLabel
            // 
            this.poweredByLabel.AutoSize = true;
            this.poweredByLabel.Location = new System.Drawing.Point(652, 681);
            this.poweredByLabel.Name = "poweredByLabel";
            this.poweredByLabel.Size = new System.Drawing.Size(146, 13);
            this.poweredByLabel.TabIndex = 2;
            this.poweredByLabel.TabStop = true;
            this.poweredByLabel.Text = "Powered By Wallpaper Abyss";
            this.poweredByLabel.LinkClicked += new System.Windows.Forms.LinkLabelLinkClickedEventHandler(this.poweredByLabel_LinkClicked);
            // 
            // iconByLabel
            // 
            this.iconByLabel.AutoSize = true;
            this.iconByLabel.Location = new System.Drawing.Point(9, 681);
            this.iconByLabel.Name = "iconByLabel";
            this.iconByLabel.Size = new System.Drawing.Size(186, 13);
            this.iconByLabel.TabIndex = 3;
            this.iconByLabel.TabStop = true;
            this.iconByLabel.Text = "Icon designed by freepik from Flaticon";
            this.iconByLabel.LinkClicked += new System.Windows.Forms.LinkLabelLinkClickedEventHandler(this.iconByLabel_LinkClicked);
            // 
            // MainWindow
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(255)))), ((int)(((byte)(248)))), ((int)(((byte)(216)))));
            this.ClientSize = new System.Drawing.Size(798, 700);
            this.Controls.Add(this.iconByLabel);
            this.Controls.Add(this.poweredByLabel);
            this.Controls.Add(this.pictureBox1);
            this.Controls.Add(this.panel1);
            this.MaximizeBox = false;
            this.Name = "MainWindow";
            this.Text = "Irpyte";
            this.panel1.ResumeLayout(false);
            this.panel1.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.progressIndicator)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.pictureBox1)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Panel panel1;
        private System.Windows.Forms.Button findNewButton;
        private System.Windows.Forms.TextBox searchBox;
        private System.Windows.Forms.Button searchButton;
        private System.Windows.Forms.PictureBox pictureBox1;
        private PictureBox progressIndicator;
        private LinkLabel poweredByLabel;
        private LinkLabel iconByLabel;
        private LinkLabel newVersionLabel;
    }
}

