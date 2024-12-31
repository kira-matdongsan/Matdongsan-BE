class Api::V1::Auth::RegistrationsController < Devise::RegistrationsController
  include ApiResponse
  include ExceptionHandler
  respond_to :json

  private

  def respond_with(current_user, _opts = {})
    if resource.persisted?
      render_json :success, data: resource
    else
      render_json :fail, message: "User couldn't be created successfully. #{current_user.errors.full_messages.to_sentence}"
    end
  end
end
