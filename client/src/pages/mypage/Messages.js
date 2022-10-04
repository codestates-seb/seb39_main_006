import axios from "axios";
import React, { useEffect, useState } from "react";
import SideBar from "../../components/SideBar";
import H1 from "../../components/ui/H1";
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
      <H1>알림들</H1>
    </div>
  );
};

export default Messages;
