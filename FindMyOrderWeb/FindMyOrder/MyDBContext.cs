using FindMyOrder.Models;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Data.Entity.ModelConfiguration.Conventions;
using System.Linq;
using System.Web;

namespace FindMyOrder
{
    public class MyDBContext : DbContext
    {

        static MyDBContext()
        {
            Database.SetInitializer<MyDBContext>(null);
        }

        public DbSet<User> User { get; set; }
        public DbSet<UserDetails> UserDetails { get; set; }
        public DbSet<Contact> Contact { get; set; }
        public DbSet<ReportProblemForm> ReportProblemForm { get; set; }
        public DbSet<ClientPackage> ClientPackage { get; set; }
        public DbSet<UserRelationship> UserRelationship { get; set; }
        public DbSet<CourierLocation> CourierLocation { get; set; }
        public DbSet<Cars> Cars { get; set; }
        public DbSet<DeliveredPackages> DeliveredPackages { get; set; }


        


        protected override void OnModelCreating(DbModelBuilder modelBuilder)
        {

            modelBuilder.Entity<UserDetails>().HasKey(t => t.Id);
            modelBuilder.Entity<Cars>().HasKey(t => t.Id);
            modelBuilder.Entity<User>().HasKey(t => t.Id);

            //one-to-one
            modelBuilder.Entity<UserDetails>()
                        .HasRequired(s => s.User)
                        .WithOptional(s => s.UserDetails)
                        .Map(m => m.MapKey("UserId"));

            // configures one-to-many relationship
            modelBuilder.Entity<Contact>()
                .HasRequired<User>(s => s.User)
                .WithMany(g => g.ContactMessages)
                .HasForeignKey<int>(s => s.UserId);

            modelBuilder.Entity<User>()
                .HasRequired<Cars>(s => s.Car)
                .WithMany(g => g.Users)
                .HasForeignKey<int?>(s => s.CarId);

            modelBuilder.Entity<ReportProblemForm>()
               .HasRequired<User>(s => s.User)
               .WithMany(g => g.ProblemMessages)
               .HasForeignKey<int>(s => s.UserId);

            modelBuilder.Entity<ClientPackage>()
             .HasRequired<User>(s => s.User)
             .WithMany(g => g.ClientPackages)
             .HasForeignKey<int>(s => s.ClientId);

            modelBuilder.Entity<CourierLocation>()
              .HasRequired<User>(s => s.Courier)
              .WithMany(g => g.CourierLocations)
              .HasForeignKey<int>(s => s.CourierId);

            modelBuilder.Entity<UserRelationship>()
            .HasRequired<User>(s => s.Client)
            .WithMany(g => g.ClientUserRel)
            .HasForeignKey<int>(s => s.ClientId);

            modelBuilder.Entity<UserRelationship>()
            .HasRequired<User>(s => s.Courier)
            .WithMany(g => g.CourierUserRel)
            .HasForeignKey<int>(s => s.CourierId);
        }
    }
}