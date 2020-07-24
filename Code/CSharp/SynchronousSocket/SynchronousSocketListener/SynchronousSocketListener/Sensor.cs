using System;
using System.Collections.Generic;
using System.Text;

    public class Sensor
    {

    public int Id;
    public string Desc;
    public string Type;
    public string Frequency;

    public Sensor(int id, string desc, string type, string frequency)
    {
        Id = id;
        Desc = desc;
        Type = type;
        Frequency = frequency;
    }

}

