package me.brecher.blackjack.server.handmanager;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class HandManagerModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().implement(HandManager.class, HandManagerImpl.class).build(HandManagerFactory.class));
    }
}
