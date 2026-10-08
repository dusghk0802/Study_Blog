package chap12._02_collection._03_hashset;

import chap12._02_collection._04_treeset.Member;

public class MemberHashSetTest {
    public static void main(String[] args) {

        MemberHashSet memberHashSet = new MemberHashSet();

        Member memberLee = new Member(1001, "이순신");
        Member memberPark = new Member(1002, "박순신");
        Member memberKim = new Member(1003, "김순신");

        memberHashSet.addMember(memberLee);
        memberHashSet.addMember(memberPark);
        memberHashSet.addMember(memberKim);
        memberHashSet.showAllMember();

    }
}
