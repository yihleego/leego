package io.leego.commons.sequence.provider;

/**
 * @author Leego Yih
 */
public abstract class AbstractSequenceProvider implements SequenceProvider {

    @Override
    public Long next(String key) {
        return this.next(key, 1).getBegin();
    }

}
