$(document).ready(function() {

  //CHAMADA PRETTYPHOTO
  $("a[rel^='prettyPhoto']").prettyPhoto({
    social_tools: false
  });

  //BANNER HOME
  $(".owl-carousel").owlCarousel({
      slideSpeed : 300,
      paginationSpeed : 400,
      singleItem:true,
      autoPlay: 5000,
      transitionStyle : "fade"
  });

});