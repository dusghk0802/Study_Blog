package chap12._02_collection._01_arraylist;

import chap12._02_collection._04_treeset.Member;

public class MemberArrayListTest {
    public static void main(String[] args) {
        MemberArrayList memberArrayList = new MemberArrayList();
        Member memberLee = new Member(1001, "이순신");
        Member memberSon = new Member(1002, "손순신");
        Member memberPark = new Member(1003, "박순신");
        Member memberHong = new Member(1004, "홍순신");

        memberArrayList.addMember(memberLee);
        memberArrayList.addMember(memberSon);
        memberArrayList.addMember(memberPark);
        memberArrayList.addMember(memberHong);

        memberArrayList.showAllMember();
        memberArrayList.removeMember(memberHong.getMemberId());
        memberArrayList.showAllMember();
    }
}
