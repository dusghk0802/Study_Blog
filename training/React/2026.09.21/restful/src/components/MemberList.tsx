import { useEffect, useState } from "react";
import "./MemberList.css";

interface Member {
    id: number;
    name: string;
    email: string;
    age: number;
}

function MemberList() {
    const [members, setMembers] = useState<Member[]>([]);

    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [age, setAge] = useState("");

    // 회원 목록 조회
    const getMembers = () => {
        fetch("http://localhost:8080/api/members")
            .then(response => response.json())
            .then(data => setMembers(data))
            .catch(error => console.error(error));
    };

    useEffect(() => {
        getMembers();
    }, []);

    // 회원 추가
    const addMember = () => {
        fetch("http://localhost:8080/api/members", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify([
                {
                    name: name,
                    email: email,
                    age: Number(age),
                },
            ]),
        })
            .then(response => response.json())
            .then(() => {
                setName("");
                setEmail("");
                setAge("");
                getMembers();
            })
            .catch(error => console.error(error));
    };

    return (
        <div className="memberlist-container">
            <h1>회원 관리</h1>

            {/* 회원 추가 */}
            <div className="member-form">
                <input
                    type="text"
                    placeholder="이름"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                />

                <input
                    type="email"
                    placeholder="이메일"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />

                <input
                    type="number"
                    placeholder="나이"
                    value={age}
                    onChange={(e) => setAge(e.target.value)}
                />

                <button onClick={addMember}>
                    회원 추가
                </button>
            </div>

            {/* 회원 목록 */}
            <h2>회원 목록</h2>

            <table className="memberlist-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>이름</th>
                        <th>이메일</th>
                        <th>나이</th>
                    </tr>
                </thead>

                <tbody>
                    {members.map((member) => (
                        <tr key={member.id}>
                            <td>{member.id}</td>
                            <td>{member.name}</td>
                            <td>{member.email}</td>
                            <td>{member.age}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

export default MemberList;