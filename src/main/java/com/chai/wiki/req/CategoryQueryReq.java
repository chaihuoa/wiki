package com.chai.wiki.req;

public class CategoryQueryReq extends PageReq {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append("]");
        return sb.toString();
    }
}