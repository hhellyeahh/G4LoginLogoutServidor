/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author 2dam
 */
public class UnknownModelTypeException extends Exception {

    /**
     * Creates a new instance of <code>UnknownModelTypeException</code> without
     * detail message.
     */
    public UnknownModelTypeException() {
    }

    /**
     * Constructs an instance of <code>UnknownModelTypeException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public UnknownModelTypeException(String msg) {
        super(msg);
    }
}
