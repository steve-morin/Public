using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;

public class SynchronousSocketListener
{

    // Incoming command from the client
    public static string command = null;

    static List<Sensor> sensors;

    public static void StartListening()
    {
        // Data buffer for incoming data.  
        byte[] bytes = new Byte[1024];

        // Establish the local endpoint for the socket.  
        // Dns.GetHostName returns the name of the
        // host running the application.  
        IPHostEntry ipHostInfo = Dns.GetHostEntry(Dns.GetHostName());
        IPAddress ipAddress = ipHostInfo.AddressList[0];
        IPEndPoint localEndPoint = new IPEndPoint(ipAddress, 10001);

        // Create a TCP/IP socket.  
        Socket listener = new Socket(ipAddress.AddressFamily,
            SocketType.Stream, ProtocolType.Tcp);

        // Read sensor config
        sensors = InitSensorConfig();

        // Bind the socket to the local endpoint and
        // listen for incoming connections
        try
        {
            listener.Bind(localEndPoint);
            listener.Listen(10);

            // Start listening for connections
            while (true)
            {
                Console.WriteLine("Waiting for a connection...");
                // Program is suspended while waiting for an incoming connection
                Socket handler = listener.Accept();
                command = null;

                // An incoming connection needs to be processed
                while (true)
                {
                    int bytesRec = handler.Receive(bytes);
                    command += Encoding.ASCII.GetString(bytes, 0, bytesRec);
                    if (command.IndexOf("<EOF>") > -1)
                    {
                        break;
                    }
                }

                // Show the command received from the client on the console
                Console.WriteLine("Text received : {0}", command);

                // Remove <EOF> from the message received
                command = command.Replace("<EOF>","");

                // create list of sensor data objects
                var sensordata = new List<SensorData>();

                // Generate sensor data
                sensordata = GenerateSensorData(sensors);

                // foreach sensordata objects generated, send data back to client
                foreach (SensorData sensorMember in sensordata)
                {
                    // encodes characters into a sequence of bytes
                    byte[] msg = Encoding.ASCII.GetBytes(command + "|" + "Data: "+ sensorMember.Value +" ");

                    // Send the message
                    handler.Send(msg);

                    // Slight delay to allow client to read the message
                    Thread.Sleep(100);
                }

                // Shutdown the socket
                handler.Shutdown(SocketShutdown.Both);

                // Close the socket
                handler.Close();
            }

        }
        catch (Exception e)
        {
            Console.WriteLine(e.ToString());
        }

        Console.WriteLine("\nPress ENTER to continue");
        Console.Read();

    }

    private static List<Sensor> InitSensorConfig()
    {
        //Open config file
        int counter = 0, id;
        string line, desc, type, frequency;

        var sensors = new List<Sensor>();

        // Open the configuration file 
        System.IO.StreamReader file =
            new System.IO.StreamReader(@"C:\Users\morins\source\repos\SynchronousSocketListener\SynchronousSocketListener\config\sensor_config.txt");

        while ((line = file.ReadLine()) != null)
        {
            System.Console.WriteLine(line);
            counter++;

            // split line by commas
            string[] fields = line.Split(',');
            id = Convert.ToInt32(fields[0]);
            desc = fields[1];
            type = fields[2];
            frequency = fields[3];

            // Add sensor object to the list
            sensors.Add(new Sensor(id, desc, type, frequency));
        }

        file.Close();
        System.Console.WriteLine("There were {0} lines.", counter);
        return sensors;
    }

    private static List<SensorData> GenerateSensorData(List<Sensor> sensorconfig)
    {
        var sensordata = new List<SensorData>();

        // create random sensor data for each sensor in the list
        foreach(Sensor sensor in sensorconfig)
        {
            Random value = new Random();
            double curVal = value.Next(0, 100);
            sensordata.Add(new SensorData(1, DateTime.Now , curVal, 1));            
        }

        return sensordata;

    }
    public static int Main(String[] args)
    {    
        StartListening();
        return 0;
    }

}

