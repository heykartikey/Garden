package com.example.garden.di

//
//import android.app.Application
//import android.content.Context
//import com.example.garden.R
//import com.example.garden.data.repository.FeedRepository
//import com.example.garden.data.repository.FirebaseFeedRepository
//import com.example.garden.domain.repository.AuthRepository
//import com.google.android.gms.auth.api.identity.BeginSignInRequest
//import com.google.android.gms.auth.api.identity.Identity
//import com.google.android.gms.auth.api.identity.SignInClient
//import com.google.android.gms.auth.api.signin.GoogleSignIn
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.ktx.auth
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.ktx.database
//import com.google.firebase.ktx.Firebase
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.components.ViewModelComponent
//import dagger.hilt.android.qualifiers.ApplicationContext
//import javax.inject.Named
//
//const val SIGN_IN_REQUEST = "signInRequest"
//const val SIGN_UP_REQUEST = "signUpRequest"
//
//const val FeedTable = "feed"
//
//const val USER_REF = "user_ref"
//const val UserTable = "user"
//
//@Module
//class AppModule {
//    //    @Provides
////    fun provideFirebaseAuth() = Firebase.auth
////
////    @Provides
////    fun provideFirebaseRealtime() = Firebase.database
////
//    @Provides
//    @Named("FeedDatabase")
////
////    @Provides
////    @Named("UsersDatabase")
////    fun provideFirebaseUsers() = Firebase.database.getReference(UserTable)
////
////    @Provides
////    fun provideFirebaseFeedRepository(
////        @Named("FeedDatabase") ref: DatabaseReference,
////    ): FeedRepository {
////        return FirebaseFeedRepository(ref)
////    }
////
////    @Provides
////    fun provideOneTapClient(
////        @ApplicationContext context: Context,
////    ) = Identity.getSignInClient(context)
////
////    @Provides
////    @Named(SIGN_IN_REQUEST)
////    fun provideSignInRequest(
////        app: Application,
////    ) = BeginSignInRequest.builder().setGoogleIdTokenRequestOptions(
////        BeginSignInRequest.GoogleIdTokenRequestOptions.builder().setSupported(true)
////            .setServerClientId(app.getString(R.string.default_web_client_id))
////            .setFilterByAuthorizedAccounts(true).build()
////    ).setAutoSelectEnabled(true).build()
////
////    @Provides
////    @Named(SIGN_UP_REQUEST)
////    fun provideSignUpRequest(
////        app: Application,
////    ) = BeginSignInRequest.builder().setGoogleIdTokenRequestOptions(
////        BeginSignInRequest.GoogleIdTokenRequestOptions.builder().setSupported(true)
////            .setServerClientId(app.getString(R.string.default_web_client_id))
////            .setFilterByAuthorizedAccounts(false).build()
////    ).build()
////
////    @Provides
////    fun provideGoogleSignInOptions(
////        app: Application,
////    ) = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
////        .requestIdToken(app.getString(R.string.default_web_client_id)).requestEmail().build()
////
////    @Provides
////    fun provideGoogleSignInClient(
////        app: Application,
////        options: GoogleSignInOptions,
////    ) = GoogleSignIn.getClient(app, options)
////
////    @Provides
////    fun provideAuthRepository(
////        auth: FirebaseAuth,
////        oneTapClient: SignInClient,
////        @Named(SIGN_IN_REQUEST) signInRequest: BeginSignInRequest,
////        @Named(SIGN_UP_REQUEST) signUpRequest: BeginSignInRequest,
////    ): AuthRepository = AuthRepositoryImpl(
////        auth = auth,
////        oneTapClient = oneTapClient,
////        signInRequest = signInRequest,
////        signUpRequest = signUpRequest,
////    )
////
//////    @Provides
//////    fun provideProfileRepository(
//////        auth: FirebaseAuth,
//////        oneTapClient: SignInClient,
//////        signInClient: GoogleSignInClient,
//////        db: FirebaseFirestore,
//////    ): ProfileRepository = ProfileRepositoryImpl(
//////        auth = auth, oneTapClient = oneTapClient, signInClient = signInClient, db = db
//////    )
//}