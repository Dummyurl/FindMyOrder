using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;

namespace FindMyOrder.Models
{
    [Table("CourierLocation")]
    public class CourierLocation
    {
        public int Id { get; set; }
        public double Latitude { get; set; }
        public double Longitude { get; set; }
        public double Speed { get; set; }
        public DateTime LocationTime { get; set; }

        [NotMapped]
        public string StringLocationTime { get; set; }


        public int CourierId { get; set; }
        public User Courier { get; set; }
    }
}