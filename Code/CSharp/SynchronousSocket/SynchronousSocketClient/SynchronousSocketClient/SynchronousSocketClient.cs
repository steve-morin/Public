using System;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;

public class SynchronousSocketClient
{
    public static void StartClient()
    {
        // Buffer for data read from socket
        byte[] bytes = new byte[1024];

        while (true == true)
        {

            // Connect to a remote device
            try
            {
                // Establish the remote endpoint for the socket  
                // This example uses port 10001 on the local computer
                IPHostEntry ipHostInfo = Dns.GetHostEntry(Dns.GetHostName());
                IPAddress ipAddress = ipHostInfo.AddressList[0];
                IPEndPoint remoteEP = new IPEndPoint(ipAddress, 10001);

                // Create a TCP/IP  socket
                Socket sender = new Socket(ipAddress.AddressFamily,
                    SocketType.Stream, ProtocolType.Tcp);

                // Connect the socket to the remote endpoint. Catch any errors.  
                try
                {
                    sender.Connect(remoteEP);

                    Console.WriteLine("Socket connected to {0}",
                        sender.RemoteEndPoint.ToString());

                    // Encode the command string into a byte array
                    byte[] msg = Encoding.ASCII.GetBytes("send all<EOF>");

                    // Send the data through the socket
                    int bytesSent = sender.Send(msg);

                    DateTime value = DateTime.Now;
                    String timeStamp = value.ToString("yyyy MM dd HH mm:ss:ffff");

                    int lengthReceived;
                    do
                    {
                        // Receive the response from the remote device 
                        int bytesRec = sender.Receive(bytes);

                        // get the string length
                        string msgReceived = Encoding.ASCII.GetString(bytes, 0, bytesRec);
                        lengthReceived = msgReceived.Length;

                        if (lengthReceived > 0)
                        {
//                            Console.WriteLine("Echoed test = {0}", Encoding.ASCII.GetString(bytes, 0, bytesRec));
                            int startIndex = msgReceived.IndexOf("<SOT>", 0) + 5;
                            int strLength = msgReceived.IndexOf("<EOT>", 0) - startIndex;
                            msgReceived = msgReceived.Substring(startIndex, strLength);
                            Console.WriteLine("message:" + msgReceived);
                            string [] strArray = msgReceived.Split("|");

                            int id, dataQuality;
                            double sensorData;
                            DateTime dt;
                            id = Int32.Parse(strArray[0]);
                            dataQuality = Int32.Parse(strArray[3]);
                            sensorData = Double.Parse(strArray[2]);
                            dt = DateTime.Parse(strArray[1]);
                        }

                    } while (lengthReceived > 0) ;

                    // Release the socket.  
                    sender.Shutdown(SocketShutdown.Both);
                    sender.Close();

                }
                catch (ArgumentNullException ane)
                {
                    Console.WriteLine("ArgumentNullException : {0}", ane.ToString());
                }
                catch (SocketException se)
                {
                    Console.WriteLine("SocketException : {0}", se.ToString());
                }
                catch (Exception e)
                {
                    Console.WriteLine("Unexpected exception : {0}", e.ToString());
                }

            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }

            // Sleep until the next cycle
            Thread.Sleep(10000);
        }

    }

    public static int Main(String[] args)
    {
        StartClient();
        return 0;
    }
}