package appjpm4everyone.baseprojectkotlin.ui.server.dogs

import android.widget.Toast
import appjpm4everyone.baseprojectkotlin.ui.server.GetDogs
import appjpm4everyone.baseprojectkotlin.ui.server.ServiceManager
import appjpm4everyone.data.dogsOut.DogsResponse
import appjpm4everyone.data.servicesInterfaz.DogsServicesImages
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await


class DogsServicesData : DogsServicesImages {

    override suspend fun dogsGetImages(queryDogs: String): DogsResponse =
        ServiceManager.serviceDogs.getCharacterByName("$queryDogs/images").await()

}

