import './App.css';
import AudioUploader from './AudioUploader/AudioUploader';

function App() {
  return (
    <div className="App">
      <h1 className="greeting-container"> Welcome to the Phi Store! </h1>
      <h3 className="order-container"> What would you like to eat? <br/><br/><AudioUploader /> </h3>
      {/* TODO1: how to translate audio input to an audio file */}
      {/* TODO2: need a separate page for order? e.g. phi-store.com/order */}

    </div>
  );
}

export default App;
