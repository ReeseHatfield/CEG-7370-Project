import React, { useState } from 'react';

export default function AudioUploader() {

    const [location, setLocation] = useState("");
    
    const findLocation = () => {
        fetch('https://ipapi.co/json/')
        .then((res) => res.json())
        .then((data) => {
            console.log(data.city, data.region, data.country_name);    
            setLocation(`${data.city}, ${data.region}, ${data.country_name}`)
        })
        .catch(function(error) {
            console.log(error);
        }); 
    }

    return(
        <button onClick={findLocation()} > 
            {location ? <p> {location} </p>: <p> Click to find and use your location! </p> }
        </button>

    )
}