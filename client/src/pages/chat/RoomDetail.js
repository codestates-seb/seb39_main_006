import axios from "axios";
import React, { useRef } from "react";
import { useEffect } from "react";
import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import styled from "styled-components";

const RoomDetail = () => {
	const { roomId } = useParams();
	const navigate = useNavigate();
	const scrollRef = useRef();
	const boxRef = useRef();

	const [room, setRooom] = useState({});
	const [chat, setChat] = useState([]);
	const [msg, setMsg] = useState("");

	// username 서버에서 체크하기.
	const username = sessionStorage.getItem("userName");
	const client = useRef();

	/* 
		권한 없이 접근했을 때 예외를 발생하는 방법이 뭐가 있을까.
		1. Room data를 가져온 뒤 memberId와 맞지 않는 사람은 연결 전에 뒤로 가게 하기
		2. 연결을 조건으로 처리
	*/

	useEffect(() => {
		axios(`${process.env.REACT_APP_URL}/api/chat/rooms/${roomId}`, {
			headers: {
				access_hh: sessionStorage.getItem("AccessToken"),
			},
		})
			.then((res) => {
				setRooom(res.data);
			})
			.catch((err) => {
				if (err.response.status === 500) {
					alert("서버 오류입니다. 다시 시도해주세요.");
					navigate(-1);
					return;
				}
				if (err.response.status !== 0) {
					alert(err.response.data.korMessage);
					navigate(-1);
					return;
				}
				if (err) {
					alert("잘못된 접근 방법입니다. 다시 시도해주세요.");
					navigate(-1);
					return;
				}
			});
		connect();

		return () => disconnect();
	}, [roomId]);

	const connect = () => {
		const socket = new SockJS(`${process.env.REACT_APP_URL}/stomp/chat`);
		client.current = Stomp.over(socket);
		client.current.connect(
			{
				access_hh: sessionStorage.getItem("AccessToken"),
			},
			() => {
				client.current.subscribe("/sub/chat/room/" + roomId, (chatList) => {
					setChat((chat) => [...chat, JSON.parse(chatList.body)]);
				});

				client.current.send(
					"/pub/chat/enter",
					{},
					JSON.stringify({ roomId: roomId, writer: username })
				);
			}
		);
	};

	const publish = (msg) => {
		if (!client.current.connected) return;
		client.current.send(
			"/pub/chat/message",
			{},
			JSON.stringify({ roomId: roomId, message: msg, writer: username })
		);
		setMsg("");
	};

	const disconnect = () => {
		client.current.send(
			"/pub/chat/exit",
			{},
			JSON.stringify({ roomId: roomId, writer: username })
		);
		client.current.disconnect();
	};

	const handleChange = (e) => {
		setMsg(e.target.value);
	};

	const handleSubmit = (e, msg) => {
		e.preventDefault();
		if (msg === "" || msg === " ") {
			return;
		}
		publish(msg);
	};

	// 스크롤 처리
	// 개선하기 : 스크롤 가장 밑으로 내리게 - 설정 오류인듯?
	// 일정 높이 이상 스크롤 올리면 자동 스크롤 취소
	useEffect(() => {
		scrollRef.current.scrollIntoView({
			behavior: "smooth",
			block: "end",
			inline: "nearest",
		});
	}, [msg]);

	return (
		<ChatRoomStyle>
			<h1>방 이름 : {room.name}</h1>
			<p>방 번호 : {room.roomId}</p>
			<ChatBox ref={boxRef}>
				<ul className="chatList">
					{chat.map((el, idx) =>
						el.writer === username ? (
							<li className="myChat" key={idx}>
								{el.message}
							</li>
						) : (
							<li className="otherChat" key={idx}>
								{el.message}
							</li>
						)
					)}
					<div ref={scrollRef} />
				</ul>
			</ChatBox>
			<ChatInput onSubmit={(e) => handleSubmit(e, msg)}>
				<input
					type="text"
					className="chatInput"
					onChange={handleChange}
					value={msg}
					placeholder="메세지를 입력하세요."
				/>
				<button className="sendChat" type="submit">
					전송
				</button>
			</ChatInput>
		</ChatRoomStyle>
	);
};

export default React.memo(RoomDetail);

const ChatRoomStyle = styled.div`
	width: 87.72% !important;
	min-width: 480px;
	max-width: 1024px;
	max-height: 768px;
	margin: 0 auto;
	display: flex;
	flex-direction: column;
`;

const ChatBox = styled.div`
	overflow: auto;

	ul {
		padding: 1rem;
		display: flex;
		flex-direction: column;

		li {
			margin: 0.25rem 0;
			padding: 0.8rem;
			border-radius: 8px;
			box-shadow: 0 1px 4px rgba(0, 0, 0, 0.6);
		}

		.myChat {
			margin-left: auto;
			background: yellow;
		}

		.otherChat {
			margin-right: auto;
			background: rgba(0, 0, 0, 0.1);
		}
	}

	::-webkit-scrollbar {
		width: 20px;
	}

	::-webkit-scrollbar-track {
		background-color: transparent;
	}

	::-webkit-scrollbar-thumb {
		background-color: #d6dee1;
		border-radius: 20px;
		border: 6px solid transparent;
		background-clip: content-box;
	}

	::-webkit-scrollbar-thumb:hover {
		background-color: #a8bbbf;
	}
`;

const ChatInput = styled.form`
	margin: 2rem 0;
	display: flex;
	min-height: 5rem;
	input {
		flex: 5;
	}

	button {
		flex: 1;
	}
`;
