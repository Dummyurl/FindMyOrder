using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace WebAPI.Models
{

    public class ChatMessages
    {
        public long Id { get; set; }
        public string ChatMessage { get; set; }
        public DateTime ChatTime { get; set; }


        public int SenderId { get; set; }
        public User Sender { get; set; }
        public int ReceiverId { get; set; }
    }
}
