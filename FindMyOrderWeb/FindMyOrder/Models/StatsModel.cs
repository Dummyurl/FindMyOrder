using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace FindMyOrder.Models
{
    public class StatsModel
    {
        public int CourierId { get; set; }
        public List<CourierLocation> CourierLocations { get; set; }

        public int NrOfDeliveredPackages { get; set; }


        //km parcursi
        public double Mileage { get; set; }
        public double AproxSpentFuel { get; set; }


        [DataType(DataType.Date)]
        public DateTime ChosenDate { get; set; }

        public string Observations { get; set; }
    }
}