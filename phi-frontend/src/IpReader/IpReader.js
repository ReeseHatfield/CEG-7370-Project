/* 
 * Source: https://www.youtube.com/watch?v=VK9F8BWrOgY
 * Source: https://dev.to/choiruladamm/how-to-use-geolocation-api-using-reactjs-ndk
 * TODO 1: convert coord into the actual area (e.g. Dayton, OH)
 * TODO 2: send the actual area to the back end
 */

import { useState } from "react";

export default function IpReader() {
    // to track the error easily when this is combined with other codes
    const [status, setStatus] = useState(null); 
    const [coords, setCoords] = useState(null);

    // get coordinate from the public IP address
    const getLocation = () => {
        if (!navigator.geolocation) {
            // what to do if it's not supported
            setStatus("Geo-location is not supported by this browser.");
            return;
        }
        else {
            navigator.geolocation.getCurrentPosition(
                // what to do once getting the position
                (position) => {
                    const { latitude, longitude } = position.coords;
                    setCoords({ latitude, longitude });
                },
                (error) => {
                    // set a status so that an error happened here in IpReader.js
                    setStatus("Error in getting current user location :: IpReader.js: ", error)
                }
            );
        }
    };
 
    return (
        <div>
            <button onClick={getLocation} className="IpReader-button">
                Find My Location
            </button>

            <p className="IpReader-status">{status}</p>

            {coords && (
                <div>
                <p>Your current location is... </p>
                <p>Latitude: {coords.latitude}, Longitude: {coords.longitude}</p>
                </div>
            )}
        </div>
    );
}