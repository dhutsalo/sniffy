package io.sniffy.nio;

import io.sniffy.util.ExceptionUtil;
import io.sniffy.util.ReflectionCopier;
import io.sniffy.util.ReflectionUtil;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.nio.channels.spi.AbstractSelector;
import java.nio.channels.spi.SelectorProvider;
import java.util.Set;
import java.util.function.Consumer;

public class SniffySelector extends AbstractSelector {

    private final AbstractSelector delegate;

    private static final ReflectionCopier<AbstractSelector> abstractSelectorFieldsCopier = new ReflectionCopier<AbstractSelector>(AbstractSelector.class, "provider");

    private void copyToDelegate() {
        abstractSelectorFieldsCopier.copy(this, delegate);
    }

    private void copyFromDelegate() {
        abstractSelectorFieldsCopier.copy(delegate, this);
    }


    public SniffySelector(SelectorProvider provider, AbstractSelector delegate) {
        super(provider);
        this.delegate = delegate;
    }

    @Override
    protected void implCloseSelector() throws IOException {
        try {
            copyToDelegate();
            ReflectionUtil.invokeMethod(AbstractSelector.class, delegate, "implCloseSelector", Void.class);
        } catch (Exception e) {
            throw ExceptionUtil.processException(e);
        } finally {
            copyFromDelegate();
        }
    }

    @Override
    protected SelectionKey register(AbstractSelectableChannel ch, int ops, Object att) {
        try {
            copyToDelegate();
            return ReflectionUtil.invokeMethod(AbstractSelector.class, delegate, "register",
                    AbstractSelectableChannel.class, ch,
                    Integer.TYPE, ops,
                    Object.class, att,
                    SelectionKey.class
            );
        } catch (Exception e) {
            throw ExceptionUtil.processException(e);
        } finally {
            copyToDelegate();
        }
    }

    @Override
    public Set<SelectionKey> keys() {
        try {
            copyToDelegate();
            return delegate.keys();
        } finally {
            copyFromDelegate();
        }
    }

    @Override
    public Set<SelectionKey> selectedKeys() {
        try {
            copyToDelegate();
            return delegate.selectedKeys();
        } finally {
            copyToDelegate();
        }
    }

    @Override
    public int selectNow() throws IOException {
        try {
            copyToDelegate();
            return delegate.selectNow();
        } finally {
            copyFromDelegate();
        }
    }

    @Override
    public int select(long timeout) throws IOException {
        try {
            copyToDelegate();
            return delegate.select(timeout);
        } finally {
            copyFromDelegate();
        }
    }

    @Override
    public int select() throws IOException {
        try {
            copyToDelegate();
            return delegate.select();
        } finally {
            copyFromDelegate();
        }
    }

    @Override
    public Selector wakeup() {
        try {
            copyToDelegate();
            delegate.wakeup();
            return this;
        } finally {
            copyFromDelegate();
        }
    }

    // Note: this method was absent in earlier JDKs so we cannot use @Override annotation
    //@Override
    public int select(Consumer<SelectionKey> action, long timeout) throws IOException {
        try {
            copyToDelegate();
            return ReflectionUtil.invokeMethod(Selector.class, delegate, "select",
                    Consumer.class, action,
                    Long.TYPE, timeout,
                    Integer.TYPE
            );
        } catch (Exception e) {
            throw ExceptionUtil.processException(e);
        } finally {
            copyFromDelegate();
        }
    }

    // Note: this method was absent in earlier JDKs so we cannot use @Override annotation
    //@Override
    public int select(Consumer<SelectionKey> action) throws IOException {
        try {
            copyToDelegate();
            return ReflectionUtil.invokeMethod(Selector.class, delegate, "select",
                    Consumer.class, action,
                    Integer.TYPE
            );
        } catch (Exception e) {
            throw ExceptionUtil.processException(e);
        } finally {
            copyFromDelegate();
        }
    }

    // Note: this method was absent in earlier JDKs so we cannot use @Override annotation
    //@Override
    public int selectNow(Consumer<SelectionKey> action) throws IOException {
        try {
            copyToDelegate();
            return ReflectionUtil.invokeMethod(Selector.class, delegate, "selectNow",
                    Consumer.class, action,
                    Integer.TYPE
            );
        } catch (Exception e) {
            throw ExceptionUtil.processException(e);
        } finally {
            copyFromDelegate();
        }
    }
}