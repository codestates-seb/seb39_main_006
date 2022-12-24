import axios from "axios";
import React from "react";
import { useState } from "react";
import { useEffect } from "react";
import SideBar from "../../components/SideBar";
import Room from "./Room";

const Rooms = () => {
	const [rooms, setRooms] = useState({});
	const [sentRooms, setSentRooms] = useState([]);
	const [receivedRooms, setReceivedRooms] = useState([]);

	useEffect(() => {
		axios(`${process.env.REACT_APP_URL}/api/chat/rooms`, {
			headers: {
				access_hh: sessionStorage.getItem("AccessToken"),
			},
		}).then((res) => {
			setSentRooms([...res.data.sentRoomList]);
			setReceivedRooms([...res.data.receivedRoomList]);
		});
	}, []);

	return (
		<div>
			{/* <Outlet /> */}
			{/* <input
				id="name"
				type="text"
				required
				maxLength={16}
				onChange={(e) => {
					setName(e.target.value);
				}}
				placeholder="채팅방 제목 입력"
			/>
			<button
				onClick={() => {
					submitHandler();
				}}>
				생성
			</button> */}
			<SideBar />
			<div className="roomList">
				<ul className="sentRoomList">
					{sentRooms &&
						sentRooms.map((room) => (
							<li key={room.roomId}>
								<Room room={room} />
							</li>
						))}
				</ul>
				<ul className="receivedRoomList">
					{receivedRooms &&
						receivedRooms.map((room) => (
							<li key={room.roomId}>
								<Room room={room} />
							</li>
						))}
				</ul>
			</div>
		</div>
	);
};

export default Rooms;
