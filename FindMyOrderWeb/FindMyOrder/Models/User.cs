using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.Web.Security;

namespace FindMyOrder.Models
{
    [Table("User")]
    public class User
    {
        [Key]
        [DatabaseGeneratedAttribute(DatabaseGeneratedOption.Identity)]
        public int Id { get; set; }

        [DisplayName("E-mail")]
        [Required(AllowEmptyStrings = false, ErrorMessage = "Acest camp este obligatoriu")]
        [EmailAddress(ErrorMessage = "Adresa de e-mail invalida")]
        public string Email { get; set; }

        [DisplayName("Parola")]
        [Required(AllowEmptyStrings = false, ErrorMessage = "Acest camp este obligatoriu")]
        [DataType(DataType.Password)]
        [MembershipPassword(
             MinRequiredNonAlphanumericCharacters = 0,
             ErrorMessage = "Dimensiunea parolei trebuie sa contina minim 6 caractere",
             MinRequiredPasswordLength = 6
         )]
        public string Password { get; set; }


        public string Token { get; set; }
        public bool IsCurier { get; set; }
        public string Role { get; set; }

        public UserDetails UserDetails { get; set; } = new UserDetails();


        [HiddenInput, NotMapped]
        public string ReturnUrl { get; set; }



        public int? CarId { get; set; }
        public Cars Car { get; set; }

        //Relationships


        public ICollection<Contact> ContactMessages { get; set; }
        public ICollection<ReportProblemForm> ProblemMessages { get; set; }
        public ICollection<ClientPackage> ClientPackages { get; set; }
        public ICollection<CourierLocation> CourierLocations { get; set; }
        public ICollection<UserRelationship> ClientUserRel { get; set; }
        public ICollection<UserRelationship> CourierUserRel { get; set; }
    }
}