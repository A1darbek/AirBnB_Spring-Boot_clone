import logo from './logo.svg';
import './App.css';
import React,{useState,useEffect,useCallback} from "react";
import axios from "axios";
import {useDropzone} from 'react-dropzone'

const Profiles = () => {
  const [Profiles,setProfiles] = useState([])
  const fetchProfiles = () => {
    axios.get("http://localhost:8080/api/v1/profiles").then(res => {
      console.log(res)
      setProfiles(res.data)
    });
  };
  useEffect(() => {
    fetchProfiles();
  },[]);

return Profiles.map((profile,index) => {
  return (
      <div key={index}>
          <br/>
          <br/>
        <h1>{profile.first_name}</h1>
        <p>{profile.id}</p>
          <Dropzone{...profile}/>
          <br/>

      </div>
  )
    }

)
};
 function Dropzone({profileId}) {
      const onDrop = useCallback( acceptedFiles => {
          const file = acceptedFiles[0]
          console.log(file)

          const formData = new FormData()
          formData.append("file", file)

           axios.post(
              'http://localhost:8080/api/v1/profiles/${profileId}/image/upload',
              formData,
              {
                  headers: {
                      "Content-Type": "multipart/form-data"
                  }
              }
          ).then(
              () => {
                  console.log("file upload successfully")
              }
          ).catch(err => {
              console.log(err)
          })
      }, [])
    const {getRootProps, getInputProps, isDragActive} = useDropzone({onDrop})

    return (
        <div {...getRootProps()}>
            <input {...getInputProps()} />
            {
                isDragActive ?
                    <p>Drop the image ...</p> :
                    <p>Drag 'n' drop profile image , or click to select profile image</p>
            }
        </div>
    )
}

function App() {
  return (
    <div className="App">
      <Profiles/>
    </div>
  );
}

export default App;
