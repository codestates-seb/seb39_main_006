import axios from "axios";
import React, { useEffect, useState } from "react";
import SideBar from "../../components/SideBar";

const Messages = () => {
  const [messages, setMessages] = useState([]);

  useEffect(() => {
    axios(`${process.env.REACT_APP_URL}/api/messages`, {
      headers: {
        access_hh: sessionStorage.getItem("AccessToken"),
        refresh_hh: sessionStorage.getItem("RefreshToken"),
      },
    }).then((res) => console.log(res.data.data));
  });
  return (
    <div>
      <SideBar />
      <h2>알림들</h2>
    </div>
  );
};

export default Messages;
