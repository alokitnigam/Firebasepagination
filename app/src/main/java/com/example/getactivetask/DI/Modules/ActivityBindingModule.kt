package  com.example.getactivetask.DI.Modules


import com.example.getactivetask.Views.ActivityModules.MainActivityModule
import com.example.getactivetask.Views.ActivityModules.PostActivityModule
import com.example.getactivetask.Views.MainActivity
import com.example.getactivetask.Views.PostActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun contributesMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [PostActivityModule::class])
    abstract fun contributePostActivity(): PostActivity


}