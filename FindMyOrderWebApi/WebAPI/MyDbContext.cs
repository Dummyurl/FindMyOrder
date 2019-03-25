using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using WebAPI.Models;

namespace WebAPI
{
    public class MyDbContext : DbContext
    {
        public MyDbContext(DbContextOptions<MyDbContext> options)
            : base(options)
        {
        }

        public DbSet<User> User { get; set; }
        public DbSet<UserDetails> UserDetails { get; set; }
        public DbSet<ChatMessages> ChatMessages { get; set; }
        public DbSet<UserRelationship> UserRelationship { get; set; }
        public DbSet<CourierLocation> CourierLocation { get; set; }
        public DbSet<Contact> Contact { get; set; }
        public DbSet<ReportProblemForm> ReportProblemForm { get; set; }
        public DbSet<DeliveredPackages> DeliveredPackages { get; set; }
        public DbSet<DocumentPhotos> DocumentPhotos { get; set; }
        public DbSet<ClientPackage> ClientPackage { get; set; }


        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {

            //configures one-to - one relationship
            modelBuilder.Entity<User>()
                .HasOne(s => s.UserDetails)
                .WithOne(ad => ad.User)
                .HasForeignKey<UserDetails>(ad => ad.UserId);

            modelBuilder.Entity<ClientPackage>()
                  .HasOne(p => p.UserRelationship)
                  .WithOne(b => b.ClientPackage)
                  .HasForeignKey<UserRelationship>(p => p.ClientPackageId);

            //configures one-to - many relationship
            modelBuilder.Entity<ChatMessages>()
                .HasOne(p => p.Sender)
                .WithMany(b => b.ChatMessages)
                .HasForeignKey(p => p.SenderId);

            //modelBuilder.Entity<ClientPackage>()
            //     .HasOne(p => p.Client)
            //     .WithMany(b => b.ClientPackages)
            //     .HasForeignKey(p => p.ClientId);

            modelBuilder.Entity<CourierLocation>()
                .HasOne(p => p.Courier)
                .WithMany(b => b.CourierLocation)
                .HasForeignKey(p => p.CourierId);

            modelBuilder.Entity<Contact>()
                .HasOne(p => p.User)
                .WithMany(b => b.ContactMessages)
                .HasForeignKey(p => p.UserId);

            modelBuilder.Entity<ReportProblemForm>()
              .HasOne(p => p.User)
              .WithMany(b => b.ReportProblemMessages)
              .HasForeignKey(p => p.UserId);

            modelBuilder.Entity<DocumentPhotos>()
                .HasOne(p => p.Client)
                .WithMany(b => b.DocImages)
                .HasForeignKey(p => p.ClientId);



            //////many to many user relationship
            //modelBuilder.Entity<UserRelationship>()
            //    .HasKey(ur => new { ur.ClientId, ur.CourierId });

            modelBuilder.Entity<UserRelationship>()
                .HasOne(bc => bc.Client)
                .WithMany(b => b.ClientUserRel)
                .HasForeignKey(bc => bc.ClientId);

            modelBuilder.Entity<UserRelationship>()
                .HasOne(bc => bc.Courier)
                .WithMany(c => c.CourierUserRel)
                .HasForeignKey(bc => bc.CourierId);

        }
    }
}
