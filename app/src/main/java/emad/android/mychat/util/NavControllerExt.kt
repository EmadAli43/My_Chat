package emad.android.mychat.util

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

fun NavController.navigateSaftly(
    @IdRes rasId:Int,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navExtras: Navigator.Extras? = null
){
    val action = currentDestination?.getAction(rasId) ?: graph.getAction(rasId)
    if(action != null && currentDestination?.id != action.destinationId){
        navigate(rasId, args, navOptions, navExtras)
    }
}