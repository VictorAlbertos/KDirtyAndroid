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

import app.presentation.foundation.presenter.Presenter;
import app.presentation.foundation.presenter.ViewPresenter;
import app.presentation.foundation.transformations.Transformations;
import app.presentation.foundation.widgets.Notifications;
import javax.inject.Inject;

final class LaunchPresenter extends Presenter<LaunchPresenter.View> {
  private final LaunchWireframe wireframe;

  @Inject LaunchPresenter(Transformations transformations, LaunchWireframe wireframe,
      Notifications notifications) {
    super(transformations, notifications);
    this.wireframe = wireframe;
  }

  @Override public void onBindView(LaunchPresenter.View view) {
    super.onBindView(view);
    wireframe.dashboard().subscribe();
  }

  interface View extends ViewPresenter {}
}
