// https://ipapi.co/api/?javascript#complete-location5

fetch('https://ipapi.co/json/')
 .then((res) => res.json())
 .then((data) => {
    console.log(data.city, data.region, data.country_name);    
 })
 .catch(function(error) {
    console.log(error);
 }); 
