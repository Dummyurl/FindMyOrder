using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace WebAPI.Models
{
    public class DocumentPhotos
    {

        public int Id { get; set; }
        public string Title { get; set; }

        [NotMapped]
        public string DocStringImage { get; set; }

        public byte[] DocImage { get; set; }

        public int ClientId { get; set; }
        public User Client { get; set; }
    }
}
