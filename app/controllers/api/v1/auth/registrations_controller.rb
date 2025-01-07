class Api::V1::Auth::RegistrationsController < Devise::RegistrationsController
  include ApiResponse
  include ExceptionHandler
  respond_to :json

  # TODO: 이메일 회원가입 시 프로필 데이터 추가 필요

  private

  def respond_with(current_user, _opts = {})
    if resource.persisted?
      render_json :success, data: true
    else
      render_json :fail, message: "User couldn't be created successfully. #{current_user.errors.full_messages.to_sentence}"
    end
  end
end
