# PixabayApp

## Description

- The user is able to search for images entering one or more words.
- Request the Pixabay API to show the images associated with the text provided by the user and parse the JSON response.
- The App Displays a list of results. Each entry shows:
   - A thumbnail of the image
   - The Pixabay user name
   - A list of image’s tag
- The data is cached in the database.
- With a click on a list item a dialog shows up asking the user if he wants to see more details. In case of a positive answer a new detail screen
  is opened that contains:
   - A bigger version of the image
   - The name of the user
   - A list of image’s tag
   - The number of likes
   - The number of favorites
   - The number of comments
- When the app starts it triggers a search for the string “fruits”

## Libraries

- Android Support Library
- Android Architecture Components:  LiveData + ViewModels + Room SQLite
- Android Data Binding
- Dagger 2 for dependency injection
- Retrofit for REST api communication
- Glide for image loading
- Navigation jetpack for navigating between fragments

### Architechture: MVVM

## App.gif

![PixabayApp](https://user-images.githubusercontent.com/33812602/65810719-34ecbd00-e1ae-11e9-9190-1f28979e4349.gif)



