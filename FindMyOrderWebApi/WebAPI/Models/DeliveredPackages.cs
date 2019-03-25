using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace WebAPI.Models
{
    public class DeliveredPackages
    {
        public int Id { get; set; }

        public int CourierId { get; set; }
        public User Courier { get; set; }

        public int ClientId { get; set; }
        public User Client { get; set; }

        public DateTime SubmitTime { get; set; }
        public string Observations { get; set; }
    }
}
