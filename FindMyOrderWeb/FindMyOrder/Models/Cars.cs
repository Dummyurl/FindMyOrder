using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;

namespace FindMyOrder.Models
{
    [Table("Cars")]
    public class Cars
    {
        public int Id { get; set; }

        [Required(AllowEmptyStrings = false, ErrorMessage = "Acest camp este obligatoriu")]
        [DisplayName("Model masina")]
        public string Name { get; set; }


        [Required(AllowEmptyStrings = false, ErrorMessage = "Acest camp este obligatoriu")]
        [DisplayName("Litrii de combustibil / 100 Km")]
        public double? LPerHundredKm { get; set; }


        [DisplayName("Previzualizare imagine model masina")]
        [Required(AllowEmptyStrings = false, ErrorMessage = "Acest camp este obligatoriu")]
        public byte[] CarImage { get; set; }

        [NotMapped]
        public string StringCarImage { get; set; }


        public ICollection<User> Users { get; set; }

    }
}