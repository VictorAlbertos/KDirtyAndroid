/*
 * Copyright 2016 Victor Albertos
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package app.presentation.sections.launch;

import app.presentation.foundation.notifications.Notifications;
import app.presentation.foundation.presenter.SyncView;
import app.presentation.foundation.presenter.ViewPresenter;
import app.presentation.foundation.transformations.Transformations;
import app.presentation.sections.TransformationsMock;
import io.reactivex.Observable;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public final class LaunchPresenterTest {
  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
  Transformations transformations = Mockito.spy(TransformationsMock.class);
  private LaunchPresenter launchPresenterUT;
  @Mock LaunchWireframe wireframe;
  @Mock ViewPresenter view;
  @Mock Notifications notifications;
  @Mock SyncView syncView;

  @Before public void init() {
    launchPresenterUT = new LaunchPresenter(transformations, wireframe,
        notifications, syncView);
  }

  @Test public void Verify_OnBindView() {
    when(wireframe.dashboard()).thenReturn(Observable.empty());
    launchPresenterUT.onBindView(view);

    verify(wireframe).dashboard();
  }
}
