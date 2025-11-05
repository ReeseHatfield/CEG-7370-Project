import React, { useState } from 'react';

export default function AudioUploader() {

    const [location, setLocation] = useState("");
    
    const findLocation = () => {
        fetch('https://ipwho.is')
        .then((res) => res.json())
        .then((data) => {
            console.log(data.city, data.region, data.country);    
            setLocation(`${data.city}, ${data.region}, ${data.country}`)
        })
        .catch(function(error) {
            console.log(error);
        }); 
    }

    return(
        <button onClick={findLocation} > 
            {location ? <p> {location} </p>: <p> Click to find and use your location! </p> }
        </button>

    )
}