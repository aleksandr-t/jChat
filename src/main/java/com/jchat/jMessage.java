package com.jchat;

import java.io.Serializable;

public class jMessage implements Serializable {

    private ConstantVariables.jMsgFlag _flag;
    private int _hashCode;
    private String _msg;

    public jMessage(final ConstantVariables.jMsgFlag flag, final String msg) {
        this._flag = flag;
        this._msg = msg;
        this._hashCode = this.hashCode();
    }

    public jMessage(final ConstantVariables.jMsgFlag flag) {
        this(flag, null);
    }

    public ConstantVariables.jMsgFlag getMessage() {
        return this._flag;
    }

    @Override
    public String toString() {
        if (this._msg == null)
            return "";

        return this._msg;
    }

    public void check() {
        if (this._hashCode == this.hashCode())
            return;
        this._flag = ConstantVariables.jMsgFlag.CORRUPTED;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        if (this._flag != null)
            hash = this._flag.hashCode();
        if (this._msg != null)
            hash += this._msg.hashCode();

        return hash;
    }

}
