﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.IO;
using MySql.Data.MySqlClient;

namespace netdriver
{
    class CleanUp
    {

        public static void Rmfile(String filename)
        {
            String curpath = Environment.CurrentDirectory;
            String path = curpath + "\\" + filename;
            if (File.Exists(path))
            {

                try
                {
                    File.Delete(path);
                    Console.WriteLine("Remove file:" + path + " success");
                }
                catch(IOException ioe)
                {
                    Console.WriteLine(ioe.Message);
                }  
            }
        }

        public static void CloseStreamReader(StreamReader sr)
        {
            if (sr != null)
            {
                try
                {
                    sr.Close();
                }
                catch (IOException e) {
                    Console.WriteLine(e.ToString());
                    Console.WriteLine("Close " + sr + " failed！");
                }
            }
        }
        public static void CloseStreamWriter(StreamWriter sw)
        {
            if (sw != null)
            {
                try
                {
                    sw.Close();
                }
                catch (IOException e)
                {
                    Console.WriteLine(e.ToString());
                    Console.WriteLine("Close " + sw + " failed！");
                }
            }
        }

        public static void CloseConnection(MySqlConnection conn)
        {
            if (conn != null)
            {
                try
                {
                    conn.Close();
                }
                catch (IOException e)
                {
                    Console.WriteLine(e.ToString());
                    Console.WriteLine("Close " + conn + " failed！");
                }
            }
        }

    }
}
