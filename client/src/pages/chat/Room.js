import React from "react";
import { useNavigate } from "react-router-dom";
import styled from "styled-components";

const Room = ({ room }) => {
	const navigate = useNavigate();
	return (
		<RoomStyle>
			<span className="roomName">{room.name}</span>
			<button onClick={() => navigate(`/chat/${room.roomId}`)}>입장</button>
		</RoomStyle>
	);
};

export default Room;

const RoomStyle = styled.div`
	cursor: pointer;
	martin: 0.5rem;
	border: 1px solid black;
`;
