import OperandForm from "./components/OperandForm";
import {useState} from "react";


function App() {

    // note that we lifted the state up to the App component (from the OperandForm component)
    // in order to be able to display the result after the form is submitted,
    // This is why we need to pass the function to set the state to the OperandForm component.
    const [result, setResult] = useState("");

    return (
        <div className="App">
            <OperandForm url={"/add"} receiveResult={setResult}/>
            {result ? <div className="border p-3">Result is {result}</div> : ""}

        </div>
    );
}

export default App;