Rails.application.routes.draw do
  resources :groups
  # Define your application routes per the DSL in https://guides.rubyonrails.org/routing.html

  # Reveal health status on /up that returns 200 if the app boots with no exceptions, otherwise 500.
  # Can be used by load balancers and uptime monitors to verify that the app is live.
  get "up" => "rails/health#show", as: :rails_health_check

  # Defines the root path route ("/")
  # root "posts#index"

  devise_for :users, skip: [:registrations, :sessions], controllers: { omniauth_callbacks: 'api/v1/auth/omniauth_callbacks' }

  namespace :api do
    namespace :v1 do
      namespace :auth do
        devise_scope :user do
          post 'signup', to: 'registrations#create'
          post 'signin', to: 'sessions#create'
          post 'signout', to: 'sessions#destroy'
          post 'token', to: 'sessions#refresh'

          get '/kakao/callback', to: 'omniauth_callbacks#kakao'
        end

        resources :test
      end
    end
  end
end
