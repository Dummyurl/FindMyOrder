using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace WebAPI.Models
{
    public class FinalizeCommand
    {
   
        public int CourierId { get; set; }

        public int ClientId { get; set; }

        public DateTime Date { get; set; }

        public String Observations { get; set; }

    }
}
