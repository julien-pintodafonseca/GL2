package fr.ensimag.deca.context;

import java.util.ArrayList;
import java.util.List;

/**
 * Signature of a method (i.e. list of arguments)
 *
 * @author Equipe GL2
 * @date 2020
 */
public class Signature {
    List<Type> args = new ArrayList<>();

    public void add(Type t) {
        args.add(t);
    }
    
    public Type paramNumber(int n) {
        return args.get(n);
    }
    
    public int size() {
        return args.size();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Signature toCompare = (Signature) obj;
        if (this.size() != toCompare.size()) {
            return false;
        } else {
            for (int i = 0; i < this.size(); i++) {
                if (paramNumber(i) != toCompare.paramNumber(i)) {
                    return false;
                }
            }
            return true;
        }
    }
}
