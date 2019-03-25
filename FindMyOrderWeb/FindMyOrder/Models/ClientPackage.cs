using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;

namespace FindMyOrder.Models
{
    [Table("ClientPackage")]
    public class ClientPackage
    {
        public int Id { get; set; }
        [DisplayName("Nume pachet")]
        [Required(AllowEmptyStrings = false, ErrorMessage = "Acest camp este obligatoriu")]
        public string Name { get; set; }

        [DisplayName("Client")]
        [Required(AllowEmptyStrings = false, ErrorMessage = "Acest camp este obligatoriu")]
        public int ClientId { get; set; }
        public User User { get; set; }

    }
}