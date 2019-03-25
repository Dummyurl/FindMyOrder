using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;

namespace FindMyOrder.Models
{
    [Table("DeliveredPackages")]
    public class DeliveredPackages
    {
        public int Id { get; set; }
        public int CourierId { get; set; }

        public int ClientId { get; set; }

        public DateTime SubmitTime { get; set; }

        public string Observations { get; set; }
    }
}