package com.ollycredit.injection.component;

import com.ollycredit.injection.module.ServiceModule;
import com.ollycredit.injection.scope.PerActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class,modules = ServiceModule.class)
public interface ServiceComponent {



}
