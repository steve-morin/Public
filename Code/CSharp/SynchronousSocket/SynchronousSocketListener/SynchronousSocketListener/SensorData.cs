using System;
using System.Collections.Generic;
using System.Text;

    public class SensorData
    {

    public int Id;
    public DateTime Timestamp;
    public Double Value;
    public int DataQuality;

    // SensorData constructor
    public SensorData(int id, DateTime timestamp, Double value, int dataquality)
    {
        Id = id;
        Timestamp = timestamp;
        Value = value;
        DataQuality = dataquality;
    }

}

