using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;

namespace FindMyOrder.Models
{
    [Table("UserDetails")]
    public class UserDetails
    {
        [Key]
        [DatabaseGeneratedAttribute(DatabaseGeneratedOption.Identity)]
        public int Id { get; set; }

        [DisplayName("Nume")]
        [Required(AllowEmptyStrings = false, ErrorMessage = "Acest camp este obligatoriu")]
        public string Name { get; set; }
        [DisplayName("Adresa")]
        public string Address { get; set; }

        [DisplayName("Numar de telefon")]
        [Required(AllowEmptyStrings = false, ErrorMessage = "Acest camp este obligatoriu")]
        [Phone(ErrorMessage = "Numar de telefon invalid")]
        [RegularExpression(@"^\(?([0-9]{3})\)?[-. ]?([0-9]{3})[-. ]?([0-9]{4})$", ErrorMessage = "Numar de telefon invalid")]
        public string PhoneNr { get; set; }



        public virtual User User { get; set; }
    }
}