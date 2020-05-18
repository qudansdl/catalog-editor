import {
  VuexModule,
  Module,
  Mutation,
  Action,
} from 'vuex-module-decorators';
import {
  getSidebarStatus,
  getSize,
  setSidebarStatus,
  setLanguage,
  setSize,
} from '@/utils/LocalStorage';
import { Configuration } from '@/types/types';
import { getLocale } from '@/i18n';

export enum DeviceType {
  Mobile,
  Desktop,
}

export interface AppStatusI {
  device: DeviceType;
  sidebar: {
    opened: boolean;
    withoutAnimation: boolean;
  };

  language: string;
  size: string;
  status: Configuration;
  history: Configuration[];
}

@Module({ namespaced: true, name: 'status' })
class AppStatus extends VuexModule implements AppStatusI {
  public sidebar = {
    opened: getSidebarStatus() !== 'closed',
    withoutAnimation: false,
  };

  public device = DeviceType.Desktop;

  public language = getLocale();

  public size = getSize() || 'medium';

  public status: Configuration = {
    backgroundColor: '',
    backgroundImg: '',
    backgroundPattern: '',
    items: [],
  };

  public history = [] as Configuration[];

  @Mutation
  private SET_STATUS(status: Configuration) {
    this.status = status;
  }

  @Mutation
  private TOGGLE_SIDEBAR(withoutAnimation: boolean) {
    this.sidebar.opened = !this.sidebar.opened;
    this.sidebar.withoutAnimation = withoutAnimation;
    if (this.sidebar.opened) {
      setSidebarStatus('opened');
    } else {
      setSidebarStatus('closed');
    }
  }

  @Mutation
  private CLOSE_SIDEBAR(withoutAnimation: boolean) {
    this.sidebar.opened = false;
    this.sidebar.withoutAnimation = withoutAnimation;
    setSidebarStatus('closed');
  }

  @Mutation
  private TOGGLE_DEVICE(device: DeviceType) {
    this.device = device;
  }

  @Mutation
  private SET_LANGUAGE(language: string) {
    this.language = language;
    setLanguage(this.language);
  }

  @Mutation
  private SET_SIZE(size: string) {
    this.size = size;
    setSize(this.size);
  }

  @Action
  public ToggleSideBar(withoutAnimation: boolean) {
    this.TOGGLE_SIDEBAR(withoutAnimation);
  }

  @Action
  public CloseSideBar(withoutAnimation: boolean) {
    this.CLOSE_SIDEBAR(withoutAnimation);
  }

  @Action
  public ToggleDevice(device: DeviceType) {
    this.TOGGLE_DEVICE(device);
  }

  @Action
  public SetLanguage(language: string) {
    this.SET_LANGUAGE(language);
  }

  @Action
  public SetSize(size: string) {
    this.SET_SIZE(size);
  }
}

export default AppStatus;
